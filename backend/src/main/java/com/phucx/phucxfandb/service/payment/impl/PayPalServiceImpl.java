package com.phucx.phucxfandb.service.payment.impl;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import com.paypal.orders.Order;
import com.phucx.phucxfandb.constant.PayPalConstants;
import com.phucx.phucxfandb.entity.*;
import com.phucx.phucxfandb.enums.OrderType;
import com.phucx.phucxfandb.enums.PaymentStatus;
import com.phucx.phucxfandb.enums.RoleName;
import com.phucx.phucxfandb.enums.TableOccupancyStatus;
import com.phucx.phucxfandb.exception.PaymentException;
import com.phucx.phucxfandb.service.cart.CartUpdateService;
import com.phucx.phucxfandb.service.notification.SendOrderNotificationService;
import com.phucx.phucxfandb.service.notification.SendReservationNotificationService;
import com.phucx.phucxfandb.service.payment.PaymentReaderService;
import com.phucx.phucxfandb.service.payment.PaymentUpdateService;
import com.phucx.phucxfandb.service.payment.PayPalService;
import com.phucx.phucxfandb.service.table.TableOccupancyUpdateService;
import com.phucx.phucxfandb.utils.CartUtils;
import com.phucx.phucxfandb.utils.RoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayPalServiceImpl implements PayPalService {
    private final SendReservationNotificationService sendReservationNotificationService;
    private final SendOrderNotificationService sendOrderNotificationService;
    private final TableOccupancyUpdateService tableOccupancyUpdateService;
    private final PaymentUpdateService paymentUpdateService;
    private final PaymentReaderService paymentReaderService;
    private final CartUpdateService cartUpdateService;
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

        paymentUpdateService.updatePayPalOrder(paymentId, order.id(), PaymentStatus.PENDING);

        return order.links().stream()
                .filter(link -> link.rel().equals("approve"))
                .findFirst()
                .map(LinkDescription::href)
                .orElseThrow(() -> new PaymentException("No approve link found for PayPal"));
    }

    @Override
    public Order captureOrder(String paypalOrderId) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(paypalOrderId);
        request.requestBody(new OrderRequest());
        HttpResponse<Order> response = client.execute(request);
        return response.result();
    }

    private void handlePaymentCompletion(String paypalOrderId){
        try {
            Order order = captureOrder(paypalOrderId);
            String paypalStatus = order.status();

            if (PayPalConstants.COMPLETED.equalsIgnoreCase(paypalStatus)) {

                List<PurchaseUnit> purchaseUnits = order.purchaseUnits();
                if (purchaseUnits.isEmpty()) {
                    paymentUpdateService.updatePaypalPaymentStatus(paypalOrderId, PaymentStatus.FAILED);
                    throw new PaymentException("Purchase units not found in PayPal response");
                }

                List<Capture> captures = purchaseUnits.get(0).payments().captures();
                if(captures.isEmpty()){
                    paymentUpdateService.updatePaypalPaymentStatus(paypalOrderId, PaymentStatus.FAILED);
                    throw new PaymentException("Capture ID not found in PayPal response");
                }

                String captureId = captures.get(0).id();
                paymentUpdateService.updatePayPalPayment(paypalOrderId, captureId, PaymentStatus.SUCCESSFUL);
            } else {
                paymentUpdateService.updatePaypalPaymentStatus(paypalOrderId, PaymentStatus.FAILED);
                throw new PaymentException("Payment capture failed: " + paypalStatus);
            }
        } catch (IOException e) {
            paymentUpdateService.updatePaypalPaymentStatus(paypalOrderId, PaymentStatus.FAILED);
            throw new PaymentException("Error capturing payment: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void completeOrder(Authentication authentication, String paypalOrderId) {
        if(paymentReaderService.existsByPayPalOrderIdAndStatus(paypalOrderId, PaymentStatus.SUCCESSFUL)){
            return;
        }

        this.handlePaymentCompletion(paypalOrderId);
        Payment payment = paymentReaderService.getPaymentEntityByPaypalOrderId(paypalOrderId);
        if(payment.getOrder()!=null){
            com.phucx.phucxfandb.entity.Order order = payment.getOrder();

            List<RoleName> roles = RoleUtils.getRoles(authentication.getAuthorities());
            if(roles.contains(RoleName.CUSTOMER)){
                if(CartUtils.isShouldRemoveCart(payment, order.getType())){
                    List<Long> productIds = order.getOrderDetails().stream()
                            .map(OrderDetail::getProduct)
                            .map(Product::getProductId)
                            .toList();
                    cartUpdateService.removeCartItems(authentication.getName(), productIds);
                }
            }

            if(OrderType.DINE_IN.equals(order.getType())){
                TableOccupancy tableOccupancy = order.getTableOccupancy();
                tableOccupancyUpdateService.updateTableOccupancyStatus(
                        tableOccupancy.getId(),
                        tableOccupancy.getTable().getTableId(),
                        TableOccupancyStatus.CLEANING
                );
            }

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
                    reservation.getDate(),
                    payment.getMethod().getMethodName(),
                    payment.getStatus()
            );
        }else{
            throw new PaymentException("Invalid payment");
        }
    }
}
