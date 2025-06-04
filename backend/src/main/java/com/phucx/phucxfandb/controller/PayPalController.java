package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.response.PayPalRefundDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.refund.PayPalRefundService;
import com.phucx.phucxfandb.service.payment.PayPalService;
import io.swagger.v3.oas.annotations.Operation;
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
    private final PayPalRefundService payPalRefundService;

    @PostMapping("/complete")
    @Operation(summary = "Complete PayPal order", description = "Authenticated access")
    public ResponseEntity<ResponseDTO<Void>> completeOrder(@RequestParam("paypalOrderId") String paypalOrderId, Authentication authentication){
        payPalService.completeOrder(authentication, paypalOrderId);
        var response = ResponseDTO.<Void>builder()
                .message("Complete payment successfully")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/refund")
    @Operation(summary = "Refund PayPal payment", description = "Authenticated access")
    public ResponseEntity<ResponseDTO<PayPalRefundDTO>> refundPayment(@RequestParam("paymentId") String paymentId) {
        PayPalRefundDTO refundPayment = payPalRefundService.refundPayment(paymentId);
        var response = ResponseDTO.<PayPalRefundDTO>builder()
                .message("Payment refunded successfully")
                .data(refundPayment)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
