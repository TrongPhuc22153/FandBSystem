package com.phucx.phucxfoodshop.service.payment;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paypal.base.rest.PayPalRESTException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;
import com.phucx.phucxfoodshop.model.dto.PaymentDTO;
import com.phucx.phucxfoodshop.model.dto.PaymentPerMonth;
import com.phucx.phucxfoodshop.model.dto.PaymentPercentage;
import com.phucx.phucxfoodshop.model.dto.PaymentResponse;


public interface PaymentService {
    public PaymentResponse createPayment(PaymentDTO paymentDTO) throws PayPalRESTException, JsonProcessingException, NotFoundException;
    public PaymentResponse updatePaymentByOrderIDAsSuccesful(String orderID) throws PaymentNotFoundException;
    public PaymentResponse updatePaymentByOrderIDAsCanceled(String orderID) throws PaymentNotFoundException;

    public List<PaymentPerMonth> getRevenuePerMonth(Integer year);
    public List<PaymentPercentage> getPaymentPercentageByYear(Integer year);
    public List<Integer> getPaymentYears();
}
