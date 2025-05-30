package com.phucx.phucxfandb.service.payment.impl;

import com.phucx.phucxfandb.dto.request.PaymentRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.PaymentDTO;
import com.phucx.phucxfandb.entity.Payment;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.PaymentMapper;
import com.phucx.phucxfandb.repository.PaymentRepository;
import com.phucx.phucxfandb.service.payment.PaymentReaderService;
import com.phucx.phucxfandb.specifications.PaymentSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentReaderServiceImpl implements PaymentReaderService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentEntity(String paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException(Payment.class.getSimpleName(), "id", paymentId));
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentEntityByPaypalOrderId(String orderId) {
        return paymentRepository.findByPaypalOrderId(orderId)
                .orElseThrow(() -> new NotFoundException(Payment.class.getSimpleName(), "paypal order id", orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> getPayments(PaymentRequestParamsDTO params) {
        Pageable pageable = PageRequest.of(
                params.getPage(),
                params.getSize(),
                Sort.by(params.getDirection(), params.getField())
        );

        Specification<Payment> spec = Specification
                .where(PaymentSpecification.hasStatus(params.getStatus()))
                .and(PaymentSpecification.searchByOrderId(params.getOrderId()))
                .and(PaymentSpecification.hasTableNumber(params.getTableNumber()))
                .and(PaymentSpecification.searchByOrderPhone(params.getPhone()))
                .and(PaymentSpecification.searchByOrderContactName(params.getContactName()));

        return paymentRepository.findAll(spec, pageable)
                .map(paymentMapper::toPaymentEntryList);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDTO getPayment(String id) {
        return paymentRepository.findById(id)
                .map(paymentMapper::toPaymentDetail)
                .orElseThrow(() -> new NotFoundException(Payment.class.getSimpleName(), "id", id));
    }
}
