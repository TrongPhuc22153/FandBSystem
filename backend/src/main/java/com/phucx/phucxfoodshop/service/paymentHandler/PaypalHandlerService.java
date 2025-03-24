package com.phucx.phucxfoodshop.service.paymentHandler;

import com.paypal.api.payments.Payment;
import com.phucx.phucxfoodshop.model.PaymentDTO;

public interface PaypalHandlerService {
    public Payment createPayment(PaymentDTO paymentDTO, String cancelUrl, String successUrl);
    public Payment executePayment(String paymentID, String payerID);
    public void paymentSuccessfully(String paymentID, String payerID, String orderID);
    public void paymentCancelled(String orderId);
}
