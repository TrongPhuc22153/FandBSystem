package com.phucx.phucxfoodshop.service.paymentHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;

public interface PaymentHandlerService {
    public void paymentSuccessful(String orderId) throws JsonProcessingException, NotFoundException, PaymentNotFoundException;
    public void paymentFailed(String orderId) throws JsonProcessingException, NotFoundException, PaymentNotFoundException;
}
