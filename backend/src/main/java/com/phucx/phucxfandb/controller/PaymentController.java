package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.PaymentRequestParamsDTO;
import com.phucx.phucxfandb.dto.request.RequestPaymentDTO;
import com.phucx.phucxfandb.dto.response.PaymentDTO;
import com.phucx.phucxfandb.dto.response.PaymentProcessingDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.payment.PaymentProcessService;
import com.phucx.phucxfandb.service.payment.PaymentReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/payments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Payment API", description = "Payment endpoints")
public class PaymentController {
    private final PaymentReaderService paymentReaderService;
    private final PaymentProcessService paymentProcessService;

    @GetMapping
    @Operation(summary = "Get payment methods", description = "Employee access")
    public ResponseEntity<Page<PaymentDTO>> getPayments(@ModelAttribute PaymentRequestParamsDTO params) {
        Page<PaymentDTO> data = paymentReaderService.getPayments(params);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment method by ID", description = "Employee access")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable String id) {
        PaymentDTO data = paymentReaderService.getPayment(id);
        return ResponseEntity.ok().body(data);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Process payment", description = "Authenticated")
    public ResponseEntity<ResponseDTO<PaymentProcessingDTO>> processPayment(
            @PathVariable String id,
            @Valid @RequestBody RequestPaymentDTO requestPaymentDTO,
            Authentication authentication
            ) throws IOException {

        PaymentProcessingDTO data = paymentProcessService
                .processPayment(authentication, id, requestPaymentDTO);

        ResponseDTO<PaymentProcessingDTO> response = ResponseDTO.<PaymentProcessingDTO>builder()
                .message("Payment processed successfully")
                .data(data)
                .build();

        return ResponseEntity.ok().body(response);
    }

}
