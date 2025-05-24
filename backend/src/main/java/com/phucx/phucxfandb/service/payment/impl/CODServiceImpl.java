package com.phucx.phucxfandb.service.payment.impl;

import com.phucx.phucxfandb.entity.Order;
import com.phucx.phucxfandb.entity.Payment;
import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.exception.PaymentException;
import com.phucx.phucxfandb.service.notification.SendOrderNotificationService;
import com.phucx.phucxfandb.service.notification.SendReservationNotificationService;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import com.phucx.phucxfandb.service.payment.CODService;
import com.phucx.phucxfandb.service.payment.PaymentReaderService;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CODServiceImpl implements CODService {
    private final SendReservationNotificationService sendReservationNotificationService;
    private final SendOrderNotificationService sendOrderNotificationService;
    private final ReservationReaderService reservationReaderService;
    private final OrderReaderService orderReaderService;
    private final PaymentReaderService paymentReaderService;

    @Override
    public void createCODPayment(Authentication authentication, String paymentId, String orderId, String reservationId) {
        String username = authentication.getName();
        Payment payment = paymentReaderService.getPaymentEntity(paymentId);

        if(orderId!=null){
            Order order = orderReaderService.getOrderEntity(orderId);
            sendOrderNotificationService.sendPlaceOrderNotification(
                    authentication,
                    order.getOrderId(),
                    order.getType(),
                    payment.getMethod().getMethodName(),
                    payment.getStatus()
            );
        }else if (reservationId!=null){
            Reservation reservation = reservationReaderService.getReservationEntity(reservationId);
            sendReservationNotificationService.sendPlaceReservationNotification(
                    authentication,
                    reservation.getReservationId(),
                    reservation.getStartTime(),
                    payment.getMethod().getMethodName(),
                    payment.getStatus()
            );
        }else{
            throw new PaymentException("Invalid payment");
        }
    }
}
