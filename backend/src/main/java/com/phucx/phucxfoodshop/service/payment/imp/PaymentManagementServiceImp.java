package com.phucx.phucxfoodshop.service.payment.imp;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.constant.PaymentMethodConstant;
import com.phucx.phucxfoodshop.constant.PaymentStatusConstant;
import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;
import com.phucx.phucxfoodshop.model.Payment;
import com.phucx.phucxfoodshop.model.PaymentMethod;
import com.phucx.phucxfoodshop.repository.PaymentRepository;
import com.phucx.phucxfoodshop.service.payment.PaymentManagementService;
import com.phucx.phucxfoodshop.service.paymentMethod.PaymentMethodService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentManagementServiceImp implements PaymentManagementService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentMethodService paymentMethodService;

    @Override
    public Boolean updatePayment(String paymentID, String transactionID, String status) throws PaymentNotFoundException {
        log.info("updatePayment(paymentID={},transactionID={}, status={})", paymentID, transactionID, status);
        if(paymentID==null && status==null) return false;
        Payment payment = paymentRepository.findById(paymentID)
            .orElseThrow(()-> new PaymentNotFoundException("Payment " + paymentID + " does not found"));
        return paymentRepository.updatePayment(payment.getPaymentID(), transactionID, status);
    }

    @Override
    public Boolean updatePaymentStatus(String paymentID, String status) throws PaymentNotFoundException {
        log.info("updatePaymentStatus(paymentID={}, status={})", paymentID, status);
        if(paymentID==null && status==null) return false;
        Payment payment = paymentRepository.findById(paymentID)
            .orElseThrow(()-> new PaymentNotFoundException("Payment " + paymentID + " does not found"));
        return paymentRepository.updatePaymentStatus(payment.getPaymentID(), status);
    }

    @Override
    public Boolean savePayment(String paymentID, LocalDateTime paymentDate, Double amount, String status,
            String customerID, String orderID, String method) {
        log.info("savePayment(paumentID={}, paymentDate={}, amount={}, status={}, customerID={}, orderID={}, method={})", 
            paymentID, paymentDate, amount, status, customerID, orderID, method);
        if(paymentID==null) return false;
        String paymentMethod = method.toLowerCase();
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentID);
        if(optionalPayment.isPresent()) return false;
        return paymentRepository.savePayment(paymentID, paymentDate, amount, 
            customerID, orderID, status, paymentMethod);
    }

    @Override
    public Boolean savePayment(String paymentID, LocalDateTime paymentDate, String transactionID, Double amount,
            String status, String customerID, String orderID, String method) {
        log.info("savePayment(paumentID={}, paymentDate={}, transactionID={}, amount={}, status={}, customerID={}, orderID={}, method={})", 
            paymentID, paymentDate, transactionID, amount, status, customerID, orderID, method);
        if(paymentID==null) return false;
        String paymentMethod = method.toLowerCase();
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentID);
        if(optionalPayment.isPresent()) return false;
        return paymentRepository.saveFullPayment(paymentID, paymentDate, amount, 
            transactionID, customerID, orderID, status, paymentMethod);
    }

    // update payment status by orderid
    private Boolean updatePaymentByOrderIDStatus(String orderID, PaymentStatusConstant status) throws PaymentNotFoundException {
        log.info("updatePaymentByOrderIDStatus(orderID={}, status={})", status);
        if(orderID==null && status==null) return false;
        Payment payment = paymentRepository.findByOrderID(orderID)
            .orElseThrow(()-> new PaymentNotFoundException("Payment of order " + orderID + " does not found"));
        return paymentRepository.updatePaymentStatus(payment.getPaymentID(), status.name().toLowerCase());
    }

    @Override
    public Boolean updatePaymentAsSuccessfulByOrderIDPerMethod(String orderID)
            throws PaymentNotFoundException {
        log.info("updatePaymentAsSuccessfulByOrderIDPerMethod(orderID={})", orderID);
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodByOrderID(orderID);
        PaymentMethodConstant method = PaymentMethodConstant.fromString(paymentMethod.getMethodName());
        switch (method) {
            case COD:
                return this.updatePaymentByOrderIDStatus(orderID, PaymentStatusConstant.SUCCESSFUL);
            case PAYPAL:
                return true;
            case ZALOPAY:
                return true;
            default:
                break;
        }
        return false;
        
    }

    @Override
    public Boolean updatePaymentAsCanceledByOrderIDPerMethod(String orderID)
            throws PaymentNotFoundException {
        log.info("updatePaymentAsCanceledByOrderIDPerMethod(orderID={})", orderID);
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodByOrderID(orderID);
        PaymentMethodConstant method = PaymentMethodConstant.fromString(paymentMethod.getMethodName());
        switch (method) {
            case COD:
                return this.updatePaymentByOrderIDStatus(orderID, PaymentStatusConstant.CANCELLED);
            case PAYPAL:
                return this.updatePaymentByOrderIDStatus(orderID, PaymentStatusConstant.REFUND);
            case ZALOPAY:
                return this.updatePaymentByOrderIDStatus(orderID, PaymentStatusConstant.REFUND);
            default:
                break;
        }
        return false;
    }

    @Override
    public Boolean updatePaymentStatusByOrderID(String orderID, PaymentStatusConstant status) throws PaymentNotFoundException {
        log.info("updatePaymentStatusByOrderID(orderID={}, status={})", orderID, status);
        return this.updatePaymentByOrderIDStatus(orderID, status);
    }
    
}
