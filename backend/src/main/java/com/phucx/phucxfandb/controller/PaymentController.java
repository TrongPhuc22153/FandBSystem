package com.phucx.phucxfandb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {
//    private final PaymentMethodService paymentMethodService;
//    private final PaymentProcessorService paymentProcessorService;
//
//    @Operation(summary = "Pay order", tags = {"payment", "post", "customer"})
//    @PostMapping("/pay")
//    public ResponseEntity<PaymentResponse> payment(@RequestBody PaymentDTO payment,
//        HttpServletRequest request, HttpServletResponse response
//    ) throws PayPalRESTException, IOException, NotFoundException{
//        String baseUrl = ServerUrlUtils.getBaseUrl(request);
//        payment.setBaseUrl(baseUrl);
//        PaymentResponse paymentResponse = paymentProcessorService.createPayment(payment);
//        return ResponseEntity.ok().body(paymentResponse);
//    }
//
//    @Operation(summary = "Get payment methods", tags = {"payment", "get"})
//    @GetMapping("/methods")
//    public ResponseEntity<List<PaymentMethod>> getPaymentMethods(){
//        List<PaymentMethod> methods = paymentMethodService.getPaymmentMethods();
//        return ResponseEntity.ok().body(methods);
//    }
}
