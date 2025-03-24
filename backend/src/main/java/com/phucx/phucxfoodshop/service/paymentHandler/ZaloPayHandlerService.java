package com.phucx.phucxfoodshop.service.paymentHandler;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;
import com.phucx.phucxfoodshop.model.PaymentDTO;

public interface ZaloPayHandlerService {
    // create a new payment
    public String createPayment(PaymentDTO paymentDTO);
    // query payment from zalo
    public Map<String, Object> queryPayment(String transId);
    public Boolean executePayment(String orderId, String transId) throws PaymentNotFoundException, JsonProcessingException, NotFoundException;
    public String callback(String jsonStr);
}
