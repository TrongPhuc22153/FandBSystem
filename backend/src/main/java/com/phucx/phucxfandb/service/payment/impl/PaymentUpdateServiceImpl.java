package com.phucx.phucxfandb.service.payment.impl;

import com.phucx.phucxfandb.constant.PaymentStatus;
import com.phucx.phucxfandb.dto.response.PaymentDTO;
import com.phucx.phucxfandb.entity.*;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.PaymentMapper;
import com.phucx.phucxfandb.repository.PaymentRepository;
import com.phucx.phucxfandb.service.customer.CustomerReaderService;
import com.phucx.phucxfandb.service.employee.EmployeeReaderService;
import com.phucx.phucxfandb.service.payment.PaymentUpdateService;
import com.phucx.phucxfandb.service.paymentMethod.PaymentMethodReaderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentUpdateServiceImpl implements PaymentUpdateService {
    private final PaymentMethodReaderService paymentMethodReaderService;
    private final CustomerReaderService customerReaderService;
    private final EmployeeReaderService employeeReaderService;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;

    @Override
    @Transactional
    public Payment createCustomerPayment(String methodName, BigDecimal amount, String customerId) {
        PaymentMethod paymentMethod = paymentMethodReaderService
                .getPaymentMethodEntityByName(methodName);
        Customer customer = customerReaderService
                .getCustomerEntityById(customerId);

        Payment payment = Payment.builder()
                .amount(amount)
                .method(paymentMethod)
                .customer(customer)
                .build();

        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment createEmployeePayment(String methodName, BigDecimal amount, String employeeId) {
        PaymentMethod paymentMethod = paymentMethodReaderService
                .getPaymentMethodEntityByName(methodName);
        Employee employee = employeeReaderService
                .getEmployeeEntityById(employeeId);

        Payment payment = Payment.builder()
                .amount(amount)
                .method(paymentMethod)
                .employee(employee)
                .build();

        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public PaymentDTO updatePaypalPaymentStatus(String paypalOrderId, PaymentStatus status) {
        Payment payment = paymentRepository.findByPaypalOrderId(paypalOrderId)
                .orElseThrow(() -> new NotFoundException(Payment.class.getSimpleName(), "paypal order id", paypalOrderId));

        payment.setStatus(status);
        Payment updatedPayment = paymentRepository.save(payment);
        return mapper.toPaymentDTO(updatedPayment);
    }

    @Override
    @Transactional
    public PaymentDTO updatePaypalOrderId(String paymentId, String paypalOrderId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException(Payment.class.getSimpleName(), "id", paymentId));

        payment.setPaypalOrderId(paypalOrderId);
        Payment updatedPayment = paymentRepository.save(payment);
        return mapper.toPaymentDTO(updatedPayment);
    }

}
