package com.phucx.phucxfoodshop.service.paymentHandler;

import com.phucx.phucxfoodshop.model.PaymentDTO;

public interface MomoHandlerService {
    public String createPayment(PaymentDTO paymentDTO);
    public Boolean paymentSuccessfully(String orderID);
}
