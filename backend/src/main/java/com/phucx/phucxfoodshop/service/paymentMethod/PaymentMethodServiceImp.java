package com.phucx.phucxfoodshop.service.paymentMethod;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;
import com.phucx.phucxfoodshop.model.PaymentMethod;
import com.phucx.phucxfoodshop.repository.PaymentMethodRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentMethodServiceImp implements PaymentMethodService {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<PaymentMethod> getPaymmentMethods() {
        log.info("getPaymentMethods()");
        return paymentMethodRepository.findAll();
    }

    @Override
    public PaymentMethod getPaymentMethodByPaymentID(String paymentID) throws PaymentNotFoundException {
        log.info("getPaymentMethodByPaymentID(paymentID={})", paymentID);
        return paymentMethodRepository.findByPaymentID(paymentID)
            .orElseThrow(()-> new PaymentNotFoundException("Payment " + paymentID + " does not found!"));
    }

    @Override
    public PaymentMethod getPaymentMethodByOrderID(String orderID) throws PaymentNotFoundException {
        log.info("getPaymentMethodByOrderID(orderID={})", orderID);
        return paymentMethodRepository.findByOrderID(orderID)
            .orElseThrow(()-> new PaymentNotFoundException("Payment with order " + orderID + " does not found!"));
    }
    
}
