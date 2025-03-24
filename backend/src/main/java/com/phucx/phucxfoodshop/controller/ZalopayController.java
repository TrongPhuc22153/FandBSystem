package com.phucx.phucxfoodshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.PaymentNotFoundException;
import com.phucx.phucxfoodshop.service.paymentHandler.ZaloPayHandlerService;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/payment/zalopay", produces = MediaType.APPLICATION_JSON_VALUE)
public class ZalopayController {
    private final ZaloPayHandlerService zaloPayHandlerService;
    @Value("${phucx.payment-successful-url}")
    private String successfulUrl;
    @Value("${phucx.payment-canceled-url}")
    private String canceledUrl;


    @PostMapping("/callback")
    public ResponseEntity<String> callback(@RequestBody String jsonStr) {
        
        String result = zaloPayHandlerService.callback(jsonStr);
        return ResponseEntity.ok().body(result);
    }
    
    @GetMapping("/pay/payment")
    public ResponseEntity<String> executePayment(
        @RequestParam("orderId") String orderId,
        @RequestParam("app_trans_id") String transId
    ) throws PaymentNotFoundException, JsonProcessingException, NotFoundException{
        Boolean status = zaloPayHandlerService.executePayment(orderId, transId);
        String redirectUrl = status?successfulUrl:canceledUrl;

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
