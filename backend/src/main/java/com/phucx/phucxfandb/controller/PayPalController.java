package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.payment.PayPalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/paypal",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "PayPal API", description = "PayPal purchase endpoints")
public class PayPalController {
    private final PayPalService payPalService;

    @PostMapping("/capture")
    public ResponseEntity<ResponseDTO<Void>> captureOrder(@RequestParam("orderId") String orderId, Authentication authentication){
        payPalService.completeOrder(authentication, orderId);
        var response = ResponseDTO.<Void>builder()
                .message("Payment captured successfully")
                .build();
        return ResponseEntity.ok().body(response);
    }
}
