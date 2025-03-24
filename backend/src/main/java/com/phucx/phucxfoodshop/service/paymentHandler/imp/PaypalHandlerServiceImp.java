package com.phucx.phucxfoodshop.service.paymentHandler.imp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.phucx.phucxfoodshop.constant.Currency;
import com.phucx.phucxfoodshop.constant.PaymentConstant;
import com.phucx.phucxfoodshop.constant.PaymentStatusConstant;
import com.phucx.phucxfoodshop.model.PaymentDTO;
import com.phucx.phucxfoodshop.service.payment.PaymentManagementService;
import com.phucx.phucxfoodshop.service.paymentHandler.PaymentHandlerService;
import com.phucx.phucxfoodshop.service.paymentHandler.PaypalHandlerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaypalHandlerServiceImp implements PaypalHandlerService {
    @Autowired
    private APIContext apiContext;
    @Autowired
    private PaymentManagementService paymentManagementService;
    @Autowired
    private PaymentHandlerService paymentHandlerService;

    @Override
    public Payment createPayment(PaymentDTO paymentDTO, String cancelUrl, String successUrl) {
        cancelUrl = cancelUrl + "?orderId=" + paymentDTO.getOrderID();
        successUrl = successUrl + "?orderId=" + paymentDTO.getOrderID();
        log.info("createPayment(paymentDTO={},cancelUrl={}, successUrl={})", paymentDTO, cancelUrl, successUrl);
        try {
            Amount amount = new Amount();
            amount.setCurrency(Currency.USD.name());
            Double total = new BigDecimal(paymentDTO.getAmount()).setScale(2, RoundingMode.HALF_UP).doubleValue();        
            amount.setTotal(String.format(Locale.forLanguageTag(Currency.USD.name()), "%.2f", total));
            // transaction
            Transaction transaction = new Transaction();
            transaction.setDescription("Paypal Payment");
            transaction.setAmount(amount);

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);
            // payer
            Payer payer = new Payer();
            payer.setPaymentMethod(paymentDTO.getMethod());
            // payment
            Payment payment = new Payment();
            payment.setIntent(PaymentConstant.INTENT);
            payment.setPayer(payer);
            payment.setTransactions(transactions);
            // redirect
            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl(cancelUrl);
            redirectUrls.setReturnUrl(successUrl);
            payment.setRedirectUrls(redirectUrls);
            // create payment
            Payment createdPayment = payment.create(apiContext);
            // save payment
            this.savePayment(createdPayment, paymentDTO);

            return createdPayment;
        } catch (PayPalRESTException e) {
            log.error("Error: {}", e.getMessage());
            return null;
        }
        
    }
    // save payment to database
    private void savePayment(Payment payment, PaymentDTO paymentDTO){
        log.info("savePayment(payment={}, paymentDTO={})", payment, paymentDTO);
        LocalDateTime createdTime = convertLocalDateTime(payment.getCreateTime());
        // method
        String method = paymentDTO.getMethod();
        // save payment
        Boolean result = paymentManagementService.savePayment(
            payment.getId(), createdTime, paymentDTO.getAmount(), 
            payment.getState(), paymentDTO.getCustomerID(), 
            paymentDTO.getOrderID(), method);
        if(!result){
            log.error("Error during saving payment: {}", paymentDTO);
        }
    }

    @Override
    public Payment executePayment(String paymentID, String payerID) {
        log.info("executePayment(paymentID={}, payerID={})", paymentID, payerID);
        try {
            Payment payment = new Payment();
            payment.setId(paymentID);
            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerID);
            return payment.execute(apiContext, paymentExecution);
        } catch (PayPalRESTException e) {
            log.error("Error: {}", e.getMessage());
            return null;
        }
        
    }
    @Override
    public void paymentSuccessfully(String paymentID, String payerID, String orderID) {
        log.info("paymentSuccessfully(paymentID={}, payerID={}, orderID={})", paymentID, payerID, orderID);
        try {     
            Payment payment = this.executePayment(paymentID, payerID);
            if(payment.getState().equals("approved")){
                // get transaction
                Transaction transaction = payment.getTransactions().get(0);
                // get transaction details
                String saleID = transaction.getRelatedResources().get(0).getSale().getId();
                // save payment details to db
                paymentManagementService.updatePayment(payment.getId(), saleID, 
                    PaymentStatusConstant.SUCCESSFUL.name().toLowerCase());
                // handle payment as successful
                paymentHandlerService.paymentSuccessful(orderID);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        } 
    }
    // convert string to localdatetime
    private LocalDateTime convertLocalDateTime(String datetime){
        OffsetDateTime odt = OffsetDateTime.parse(datetime);
        LocalDateTime createdTime = odt.toLocalDateTime();
        return createdTime;
    }

    @Override
    public void paymentCancelled(String orderId) {
        log.info("paymentCancelled(orderId={})", orderId);
        try {
            paymentHandlerService.paymentFailed(orderId);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }
}
