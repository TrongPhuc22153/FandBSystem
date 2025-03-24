package com.phucx.phucxfoodshop.service.paymentMethod;

import java.util.List;

import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;
import com.phucx.phucxfoodshop.model.PaymentMethod;

public interface PaymentMethodService {
    public List<PaymentMethod> getPaymmentMethods();   
    public PaymentMethod getPaymentMethodByPaymentID(String paymentID) throws PaymentNotFoundException;
    public PaymentMethod getPaymentMethodByOrderID(String orderID) throws PaymentNotFoundException;
}
