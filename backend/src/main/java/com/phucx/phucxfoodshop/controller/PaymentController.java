package com.phucx.phucxfoodshop.controller;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.base.rest.PayPalRESTException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.dto.PaymentDTO;
import com.phucx.phucxfoodshop.model.dto.PaymentResponse;
import com.phucx.phucxfoodshop.model.entity.PaymentMethod;
import com.phucx.phucxfoodshop.service.payment.PaymentProcessorService;
import com.phucx.phucxfoodshop.service.paymentMethod.PaymentMethodService;
import com.phucx.phucxfoodshop.utils.ServerUrlUtils;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/payment", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {
    private final PaymentMethodService paymentMethodService;
    private final PaymentProcessorService paymentProcessorService;

    @Operation(summary = "Pay order", tags = {"payment", "post", "customer"})
    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> payment(@RequestBody PaymentDTO payment, 
        HttpServletRequest request, HttpServletResponse response
    ) throws PayPalRESTException, IOException, NotFoundException{
        String baseUrl = ServerUrlUtils.getBaseUrl(request);
        payment.setBaseUrl(baseUrl);
        PaymentResponse paymentResponse = paymentProcessorService.createPayment(payment);
        return ResponseEntity.ok().body(paymentResponse);
    }

    @Operation(summary = "Get payment methods", tags = {"payment", "get"})
    @GetMapping("/methods")
    public ResponseEntity<List<PaymentMethod>> getPaymentMethods(){
        List<PaymentMethod> methods = paymentMethodService.getPaymmentMethods();
        return ResponseEntity.ok().body(methods);
    }
}
