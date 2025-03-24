package com.phucx.phucxfoodshop.service.payment.imp;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paypal.base.rest.PayPalRESTException;
import com.phucx.phucxfoodshop.constant.PaymentStatusConstant;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;
import com.phucx.phucxfoodshop.model.PaymentDTO;
import com.phucx.phucxfoodshop.model.PaymentPerMonth;
import com.phucx.phucxfoodshop.model.PaymentPercentage;
import com.phucx.phucxfoodshop.model.PaymentResponse;
import com.phucx.phucxfoodshop.service.payment.PaymentManagementService;
import com.phucx.phucxfoodshop.service.payment.PaymentProcessorService;
import com.phucx.phucxfoodshop.service.payment.PaymentService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentServiceImp implements PaymentService{
    @Autowired
    private PaymentProcessorService paymentProcessorService;
    @Autowired
    private PaymentManagementService paymentManagementService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PaymentResponse createPayment(PaymentDTO paymentDTO) throws PayPalRESTException, JsonProcessingException, NotFoundException {
        log.info("createPayment(paymentDTO={})", paymentDTO);
        PaymentResponse paymentResponse = paymentProcessorService.createPayment(paymentDTO);
        return paymentResponse;
    }

    @Override
    public PaymentResponse updatePaymentByOrderIDAsSuccesful(String orderID) throws PaymentNotFoundException {
        log.info("updatePaymentByOrderIDAsSuccesful(orderID={})", orderID);
        Boolean result = this.paymentManagementService.updatePaymentAsSuccessfulByOrderIDPerMethod(orderID);
        PaymentResponse response = new PaymentResponse();
        response.setStatus(result);
        return response;
    }

    @Override
    public PaymentResponse updatePaymentByOrderIDAsCanceled(String orderID) throws PaymentNotFoundException {
        log.info("updatePaymentByOrderIDAsCanceled(orderID={})", orderID);
        Boolean result = this.paymentManagementService.updatePaymentAsCanceledByOrderIDPerMethod(orderID);
        PaymentResponse response = new PaymentResponse();
        response.setStatus(result);
        return response;
    }

    public List<PaymentPerMonth> getAmountPerMonth(Integer year, String paymentStatus) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("GetAmountPerMonth")
                .registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
                .setParameter(1, year)
                .setParameter(2, paymentStatus);

        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(result -> new PaymentPerMonth((Integer) result[0], (BigDecimal)result[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentPerMonth> getRevenuePerMonth(Integer year) {
        log.info("getRevenuePerMonth(year={})", year);
        List<PaymentPerMonth> revenue = getAmountPerMonth(year, 
            PaymentStatusConstant.SUCCESSFUL.name().toLowerCase());
        return revenue;
    }

    @Override
    public List<PaymentPercentage> getPaymentPercentageByYear(Integer year) {
        log.info("getRevenuePerMonth(year={})", year);
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("GetPaymentPercentage")
                .registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)
                .setParameter(1, year);

        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(result -> new PaymentPercentage(result[0].toString(), (BigDecimal)result[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getPaymentYears() {
        StoredProcedureQuery procedureQuery = entityManager
            .createStoredProcedureQuery("GetPaymentYear");
        List<Object> results = procedureQuery.getResultList();
        return results.stream()
            .map(result -> (Integer)result)
            .collect(Collectors.toList());
    }
    
}
