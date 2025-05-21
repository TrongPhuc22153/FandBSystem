package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestPaymentOrderDTO;
import com.phucx.phucxfandb.dto.response.PayPalResponseDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.paypal.PayPalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/paypal",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "PayPal API", description = "PayPal purchase endpoints")
public class PayPalController {
    private final PayPalService payPalService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<PayPalResponseDTO>> createOrder(
             @Valid @RequestBody RequestPaymentOrderDTO requestOrderPayment) throws IOException {
        var payPalResponseDTO = payPalService.createOrder(
                requestOrderPayment.getAmount(),
                requestOrderPayment.getCurrency(),
                requestOrderPayment.getReturnUrl(),
                requestOrderPayment.getCancelUrl()
        );
        var response = ResponseDTO.<PayPalResponseDTO>builder()
                .message("Your order created successfully")
                .data(payPalResponseDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/capture")
    public ResponseEntity<ResponseDTO<Void>> captureOrder(
            @RequestParam("orderId") String orderId) throws IOException {
        var response = payPalService.captureOrder(orderId);;
        return ResponseEntity.ok().body(response);
    }
}
