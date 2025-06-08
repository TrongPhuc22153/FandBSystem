package com.phucx.phucxfandb.service.payment.impl;

import com.phucx.phucxfandb.constant.PaymentMethodConstants;
import com.phucx.phucxfandb.entity.*;
import com.phucx.phucxfandb.enums.PaymentStatus;
import com.phucx.phucxfandb.enums.RoleName;
import com.phucx.phucxfandb.exception.PaymentException;
import com.phucx.phucxfandb.service.cart.CartUpdateService;
import com.phucx.phucxfandb.service.notification.SendOrderNotificationService;
import com.phucx.phucxfandb.service.notification.SendReservationNotificationService;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import com.phucx.phucxfandb.service.payment.CODService;
import com.phucx.phucxfandb.service.payment.PaymentUpdateService;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import com.phucx.phucxfandb.utils.CartUtils;
import com.phucx.phucxfandb.utils.RoleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CODServiceImpl implements CODService {
    private final SendReservationNotificationService sendReservationNotificationService;
    private final SendOrderNotificationService sendOrderNotificationService;
    private final ReservationReaderService reservationReaderService;
    private final PaymentUpdateService paymentUpdateService;
    private final OrderReaderService orderReaderService;
    private final CartUpdateService cartUpdateService;

    @Override
    @Transactional
    public void createCODPayment(Authentication authentication, String orderId, String reservationId) {
        if(orderId!=null){
            Order order = orderReaderService.getOrderEntity(orderId);
            String paymentId = order.getPayment().getPaymentId();

            Payment newPayment = paymentUpdateService.updatePayment(
                    paymentId,
                    PaymentMethodConstants.COD,
                    PaymentStatus.PENDING);

            List<RoleName> roles = RoleUtils.getRoles(authentication.getAuthorities());
            if(roles.contains(RoleName.CUSTOMER)){
                if(CartUtils.isShouldRemoveCart(newPayment, order.getType())){
                    List<Long> productIds = order.getOrderDetails().stream()
                            .map(OrderDetail::getProduct)
                            .map(Product::getProductId)
                            .toList();
                    cartUpdateService.removeCartItems(authentication.getName(), productIds);
                }
            }

            sendOrderNotificationService.sendPlaceOrderNotification(
                    authentication,
                    order.getOrderId(),
                    order.getType(),
                    PaymentMethodConstants.COD,
                    PaymentStatus.PENDING
            );
        }else if (reservationId!=null){
            Reservation reservation = reservationReaderService.getReservationEntity(reservationId);
            String paymentId = reservation.getPayment().getPaymentId();

            paymentUpdateService.updatePayment(
                    paymentId,
                    PaymentMethodConstants.COD,
                    PaymentStatus.PENDING);

            sendReservationNotificationService.sendPlaceReservationNotification(
                    authentication,
                    reservation.getReservationId(),
                    reservation.getDate(),
                    PaymentMethodConstants.COD,
                    PaymentStatus.PENDING
            );
        }else{
            throw new PaymentException("Invalid payment");
        }
    }
}
