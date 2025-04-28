package com.phucx.phucxfandb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/payment/paypal", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaypalPaymentController {
//    private final PaypalHandlerService paypalService;
//    @Value("${phucx.payment-successful-url}")
//    private String successfulUrl;
//    @Value("${phucx.payment-canceled-url}")
//    private String canceledUrl;
//
//
//    @GetMapping("pay/cancel")
//    public ResponseEntity<String> cancel(@RequestParam(name = "token") String token,
//        @RequestParam(name = "orderId") String orderId){
//
//        paypalService.paymentCancelled(orderId);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create(canceledUrl));
//        return new ResponseEntity<>(headers, HttpStatus.FOUND);
//    }
//
//    @GetMapping("pay/successful")
//    public ResponseEntity<String> successful(
//        @RequestParam("paymentId") String paymentId,
//        @RequestParam("PayerID") String payerId,
//        @RequestParam("orderId") String orderId) throws PayPalRESTException{
//
//        paypalService.paymentSuccessfully(paymentId, payerId, orderId);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create(successfulUrl));
//        return new ResponseEntity<>(headers, HttpStatus.FOUND);
//    }
}
