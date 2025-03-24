package com.phucx.phucxfoodshop.controller;

import java.net.URI;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.phucxfoodshop.service.paymentHandler.MomoHandlerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/payment/momo", produces = MediaType.APPLICATION_JSON_VALUE)
public class MomoPaymentController {
    private final MomoHandlerService momoHandlerService;
    @Value("${phucx.payment-successful-url}")
    private String successfulUrl;

    @GetMapping("pay/successful")
    public ResponseEntity<String> successful(@RequestParam("orderId") String orderId){
        Boolean result = momoHandlerService.paymentSuccessfully(orderId);
        if(!result){
            log.error("Error while saving payment: {}", orderId);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(successfulUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
