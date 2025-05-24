package com.phucx.phucxfandb.service.payment.impl;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import com.phucx.phucxfandb.constant.PayPalConstants;
import com.phucx.phucxfandb.constant.PaymentStatus;
import com.phucx.phucxfandb.entity.Payment;
import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.exception.PaymentException;
import com.phucx.phucxfandb.service.notification.SendOrderNotificationService;
import com.phucx.phucxfandb.service.notification.SendReservationNotificationService;
import com.phucx.phucxfandb.service.payment.PaymentReaderService;
import com.phucx.phucxfandb.service.payment.PaymentUpdateService;
import com.phucx.phucxfandb.service.payment.PayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class PayPalServiceImpl implements PayPalService {
    private final SendReservationNotificationService sendReservationNotificationService;
    private final SendOrderNotificationService sendOrderNotificationService;
    private final PaymentUpdateService paymentUpdateService;
    private final PaymentReaderService paymentReaderService;
    private final PayPalHttpClient client;

    @Override
    public String createOrder(String paymentId, BigDecimal amount, String currency, String returnUrl, String cancelUrl) throws IOException {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent(PayPalConstants.CAPTURE);

        AmountWithBreakdown amountBreakdown = new AmountWithBreakdown()
                .currencyCode(currency)
                .value(String.format("%.2f", amount));

        PurchaseUnitRequest purchaseUnit = new PurchaseUnitRequest()
                .amountWithBreakdown(amountBreakdown);

        orderRequest.purchaseUnits(Collections.singletonList(purchaseUnit));

        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl);

        orderRequest.applicationContext(applicationContext);

        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);
        HttpResponse<Order> response = client.execute(request);
        Order order = response.result();

        paymentUpdateService.updatePaypalOrderId(paymentId, order.id());

        return order.links().stream()
                .filter(link -> link.rel().equals("approve"))
                .findFirst()
                .map(LinkDescription::href)
                .orElseThrow(() -> new PaymentException("No approve link found for PayPal"));
    }

    @Override
    public String captureOrder(String orderId) {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        request.requestBody(new OrderRequest());

        try {
            HttpResponse<Order> response = client.execute(request);
            Order capturedOrder = response.result();
            String paypalStatus = capturedOrder.status();

            if (PayPalConstants.COMPLETED.equalsIgnoreCase(paypalStatus)) {
                paymentUpdateService.updatePaypalPaymentStatus(orderId, PaymentStatus.SUCCESSFUL);
                return capturedOrder.id();
            } else {
                paymentUpdateService.updatePaypalPaymentStatus(orderId, PaymentStatus.FAILED);

                throw new PaymentException("Payment capture failed: " + paypalStatus);
            }
        } catch (IOException e) {
            paymentUpdateService.updatePaypalPaymentStatus(orderId, PaymentStatus.FAILED);
            throw new PaymentException("Error capturing payment: " + e.getMessage());
        }
    }

    @Override
    public void completeOrder(Authentication authentication, String paypalOrderId) {
        String username = authentication.getName();
        String capturedOrderId = this.captureOrder(paypalOrderId);
        Payment payment = paymentReaderService.getPaymentEntityByPaypalOrderId(capturedOrderId);
        if(payment.getOrder()!=null){
            com.phucx.phucxfandb.entity.Order order = payment.getOrder();
            sendOrderNotificationService.sendPlaceOrderNotification(
                    authentication,
                    order.getOrderId(),
                    order.getType(),
                    payment.getMethod().getMethodName(),
                    payment.getStatus()
            );
        }else if (payment.getReservation()!=null){
            Reservation reservation = payment.getReservation();
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
