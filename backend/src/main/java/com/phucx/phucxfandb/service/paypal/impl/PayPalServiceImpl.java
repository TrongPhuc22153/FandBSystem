package com.phucx.phucxfandb.service.paypal.impl;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import com.phucx.phucxfandb.dto.response.PayPalResponseDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.exception.PaymentException;
import com.phucx.phucxfandb.service.paypal.PayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class PayPalServiceImpl implements PayPalService {
    private final PayPalHttpClient client;

    @Override
    public PayPalResponseDTO createOrder(double amount, String currency, String returnUrl, String cancelUrl) throws IOException {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

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

        String approvedLink = order.links().stream()
                .filter(link -> link.rel().equals("approve"))
                .findFirst()
                .map(LinkDescription::href)
                .orElseThrow(() -> new PaymentException("No approve link found for PayPal"));

        return PayPalResponseDTO.builder()
                .link(approvedLink)
                .build();
    }

    @Override
    public ResponseDTO<Void> captureOrder(String orderId) throws IOException{
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        request.requestBody(new OrderRequest());
        HttpResponse<Order> response = client.execute(request);
        Order capturedOrder = response.result();
        if (!"COMPLETED".equals(capturedOrder.status())) {
            throw new PaymentException("Payment capture failed: " + capturedOrder.status());
        }
        return ResponseDTO.<Void>builder()
                .message("Payment captured successfully: " + capturedOrder.id())
                .build();
    }
}
