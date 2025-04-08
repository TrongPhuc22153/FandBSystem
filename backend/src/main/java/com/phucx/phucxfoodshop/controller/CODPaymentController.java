package com.phucx.phucxfoodshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.phucxfoodshop.model.dto.ResponseFormat;
import com.phucx.phucxfoodshop.service.paymentHandler.CODHandlerService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/payment/cod", produces = MediaType.APPLICATION_JSON_VALUE)
public class CODPaymentController {
    private final CODHandlerService codHandlerService;

    @PostMapping("pay/cancel")
    public ResponseEntity<ResponseFormat> cancel(@RequestParam("paymentId") String paymentId){
        Boolean status = codHandlerService.paymentCancelled(paymentId);

        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setStatus(status);
        responseFormat.setMessage("Payment " + paymentId + " has been canceled!");
        return ResponseEntity.ok().body(responseFormat);
        
    }

    @PostMapping("pay/successful")
    public ResponseEntity<ResponseFormat> successful(@RequestParam("paymentId") String paymentId){
        Boolean status = codHandlerService.paymentSuccessfully(paymentId);
        ResponseFormat responseFormat = new ResponseFormat();
        responseFormat.setStatus(status);
        responseFormat.setMessage("Payment " + paymentId + " has been canceled!");
        return ResponseEntity.ok().body(responseFormat);
    }
}
