package com.phucx.phucxfandb.service.payment.imp;

import com.phucx.phucxfandb.constant.PaymentStatus;
import com.phucx.phucxfandb.dto.request.RequestPaymentDTO;
import com.phucx.phucxfandb.dto.response.PaymentDTO;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.entity.Payment;
import com.phucx.phucxfandb.entity.PaymentMethod;
import com.phucx.phucxfandb.exception.EntityExistsException;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.PaymentMapper;
import com.phucx.phucxfandb.repository.PaymentRepository;
import com.phucx.phucxfandb.service.customer.CustomerReaderService;
import com.phucx.phucxfandb.service.payment.PaymentUpdateService;
import com.phucx.phucxfandb.service.paymentMethod.PaymentMethodReaderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentUpdateServiceImpl implements PaymentUpdateService {
    private final PaymentMethodReaderService paymentMethodReaderService;
    private final CustomerReaderService customerReaderService;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;

    @Override
    @Modifying
    @Transactional
    public PaymentDTO createPayment(RequestPaymentDTO createPaymentDTO) {
        log.info("createPayment(createPaymentDTO={})", createPaymentDTO);
        if (paymentRepository.existsById(createPaymentDTO.getPaymentId())) {
            throw new EntityExistsException("Payment with ID " + createPaymentDTO.getPaymentId() + " already exists");
        }
        PaymentMethod paymentMethod = paymentMethodReaderService
                .getPaymentMethodEntity(createPaymentDTO.getPaymentMethodId());
        Customer customer = customerReaderService
                .getCustomerEntityById(createPaymentDTO.getCustomerId());
        Payment payment = mapper.toPayment(createPaymentDTO, paymentMethod, customer);
        payment.setStatus(PaymentStatus.PENDING);
        Payment savedPayment = paymentRepository.save(payment);
        return mapper.toPaymentDTO(savedPayment);
    }

    @Override
    @Modifying
    @Transactional
    public PaymentDTO updateOrderPayment(String orderId, PaymentStatus status) {
        log.info("updateOrderPayment(orderId={}, status={})", orderId, status);
//        Payment payment = paymentRepository.findByOrderOrderId(orderId)
//                .orElseThrow(() -> new NotFoundException("Payment for order", orderId));
//
//        payment.setStatus(status);
//        Payment updatedPayment = paymentRepository.save(payment);
//        return mapper.toPaymentDTO(updatedPayment);
        return null;
    }

    @Override
    @Modifying
    @Transactional
    public PaymentDTO updateReservationPayment(String reservationId, PaymentStatus status) {
        log.info("updateReservationPayment(reservationId={}, status={})", reservationId, status);
//        Payment payment = paymentRepository.findByReservationReservationId(reservationId)
//                .orElseThrow(() -> new NotFoundException("Payment for reservation", reservationId));
//
//        payment.setStatus(status); // Assuming reservation sets status to PENDING
//        Payment updatedPayment = paymentRepository.save(payment);
//        return mapper.toPaymentDTO(updatedPayment);

        return null;
    }

}
