package com.phucx.phucxfandb.service.payment.impl;

import com.phucx.phucxfandb.constant.PaymentMethodConstants;
import com.phucx.phucxfandb.dto.event.PlaceOrderNotificationEvent;
import com.phucx.phucxfandb.dto.event.PlaceReservationNotificationEvent;
import com.phucx.phucxfandb.entity.TableOccupancy;
import com.phucx.phucxfandb.enums.PaymentStatus;
import com.phucx.phucxfandb.enums.TableOccupancyStatus;
import com.phucx.phucxfandb.entity.Order;
import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.exception.PaymentException;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import com.phucx.phucxfandb.service.payment.CashService;
import com.phucx.phucxfandb.service.payment.PaymentUpdateService;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import com.phucx.phucxfandb.service.table.TableOccupancyUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CashServiceImpl implements CashService {
    private final ReservationReaderService reservationReaderService;
    private final PaymentUpdateService paymentUpdateService;
    private final OrderReaderService orderReaderService;
    private final TableOccupancyUpdateService tableOccupancyUpdateService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void createCashPayment(Authentication authentication, String orderId, String reservationId) {
        if(orderId!=null){
            Order order = orderReaderService.getOrderEntity(orderId);
            String paymentId = order.getPayment().getPaymentId();
            TableOccupancy tableOccupancy = order.getTableOccupancy();

            tableOccupancyUpdateService.updateTableOccupancyStatus(
                    tableOccupancy.getId(),
                    tableOccupancy.getTable().getTableId(),
                    TableOccupancyStatus.CLEANING);

            paymentUpdateService.updatePayment(
                    paymentId,
                    PaymentMethodConstants.CASH,
                    PaymentStatus.SUCCESSFUL);

            eventPublisher.publishEvent(
                    PlaceOrderNotificationEvent.builder()
                            .authentication(authentication)
                            .orderId(order.getOrderId())
                            .orderType(order.getType())
                            .paymentMethod(PaymentMethodConstants.CASH)
                            .paymentStatus(PaymentStatus.SUCCESSFUL)
                            .build()
            );
        }else if (reservationId!=null){
            Reservation reservation = reservationReaderService.getReservationEntity(reservationId);
            String paymentId = reservation.getPayment().getPaymentId();
            TableOccupancy tableOccupancy = reservation.getTableOccupancy();

            tableOccupancyUpdateService.updateTableOccupancyStatus(
                    tableOccupancy.getId(),
                    tableOccupancy.getTable().getTableId(),
                    TableOccupancyStatus.CLEANING);

            paymentUpdateService.updatePayment(
                    paymentId,
                    PaymentMethodConstants.CASH,
                    PaymentStatus.SUCCESSFUL);

            eventPublisher.publishEvent(
                    PlaceReservationNotificationEvent.builder()
                            .authentication(authentication)
                            .reservationId(reservation.getReservationId())
                            .reservationDate(reservation.getDate())
                            .paymentMethod(PaymentMethodConstants.CASH)
                            .paymentStatus(PaymentStatus.SUCCESSFUL)
                            .build());

        }else{
            throw new PaymentException("Invalid payment");
        }
    }
}
