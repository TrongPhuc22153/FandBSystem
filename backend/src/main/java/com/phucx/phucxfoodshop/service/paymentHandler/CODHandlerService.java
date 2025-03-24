package com.phucx.phucxfoodshop.service.paymentHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.Payment;
import com.phucx.phucxfoodshop.model.PaymentDTO;

public interface CODHandlerService {
    public Payment createPayment(PaymentDTO paymentDTO) throws JsonProcessingException, NotFoundException;
    public Boolean paymentSuccessfully(String paymentID);
    public Boolean paymentCancelled(String paymentID);
}
