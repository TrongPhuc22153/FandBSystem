package com.phucx.phucxfandb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/payment/zalopay", produces = MediaType.APPLICATION_JSON_VALUE)
public class ZalopayController {
//    private final ZaloPayHandlerService zaloPayHandlerService;
//    @Value("${phucx.payment-successful-url}")
//    private String successfulUrl;
//    @Value("${phucx.payment-canceled-url}")
//    private String canceledUrl;
//
//
//    @PostMapping("/callback")
//    public ResponseEntity<String> callback(@RequestBody String jsonStr) {
//
//        String result = zaloPayHandlerService.callback(jsonStr);
//        return ResponseEntity.ok().body(result);
//    }
//
//    @GetMapping("/pay/payment")
//    public ResponseEntity<String> executePayment(
//        @RequestParam("orderId") String orderId,
//        @RequestParam("app_trans_id") String transId
//    ) throws PaymentNotFoundException, JsonProcessingException, NotFoundException{
//        Boolean status = zaloPayHandlerService.executePayment(orderId, transId);
//        String redirectUrl = status?successfulUrl:canceledUrl;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create(redirectUrl));
//        return new ResponseEntity<>(headers, HttpStatus.FOUND);
//    }
}
