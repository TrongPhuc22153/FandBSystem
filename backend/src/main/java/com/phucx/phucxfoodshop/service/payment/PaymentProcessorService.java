package com.phucx.phucxfoodshop.service.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paypal.base.rest.PayPalRESTException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.dto.PaymentDTO;
import com.phucx.phucxfoodshop.model.dto.PaymentResponse;

public interface PaymentProcessorService {
    public PaymentResponse createPayment(PaymentDTO payment) throws PayPalRESTException, JsonProcessingException, NotFoundException;
}
