package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestPaymentMethodDTO;
import com.phucx.phucxfandb.dto.response.PaymentMethodDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.paymentMethod.PaymentMethodReaderService;
import com.phucx.phucxfandb.service.paymentMethod.PaymentMethodUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/payment-methods",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Payment Methods", description = "Public and Admin operations for payment methods")
public class PaymentMethodController {
    private final PaymentMethodReaderService paymentMethodReaderService;
    private final PaymentMethodUpdateService paymentMethodUpdateService;

    @GetMapping
    @Operation(summary = "Get payment methods", description = "Public access")
    public ResponseEntity<List<PaymentMethodDTO>> getPaymentMethods() {
        List<PaymentMethodDTO> data = paymentMethodReaderService.getPaymentMethods();
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment method by ID", description = "Public access")
    public ResponseEntity<PaymentMethodDTO> getPaymentMethod(@PathVariable String id) {
        PaymentMethodDTO data = paymentMethodReaderService.getPaymentMethod(id);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Update payment method", description = "Admin access")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<PaymentMethodDTO>> updatePaymentMethod(
            @Valid @RequestBody RequestPaymentMethodDTO requestPaymentMethodDTO,
            @PathVariable String id
    ) {
        PaymentMethodDTO updatedPaymentMethod = paymentMethodUpdateService.updatePaymentMethod(id, requestPaymentMethodDTO);
        ResponseDTO<PaymentMethodDTO> responseDTO = ResponseDTO.<PaymentMethodDTO>builder()
                .message("Payment method updated successfully")
                .data(updatedPaymentMethod)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Operation(summary = "Create new payment method", description = "Admin access")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<PaymentMethodDTO>> createPaymentMethod(
            @Valid @RequestBody RequestPaymentMethodDTO requestPaymentMethodDTO
    ) {
        PaymentMethodDTO newPaymentMethod = paymentMethodUpdateService.createPaymentMethod(requestPaymentMethodDTO);
        ResponseDTO<PaymentMethodDTO> responseDTO = ResponseDTO.<PaymentMethodDTO>builder()
                .message("Payment method created successfully")
                .data(newPaymentMethod)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(summary = "Create new payment methods", description = "Admin access")
    @PostMapping(value = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<List<PaymentMethodDTO>>> createPaymentMethods(
            @NotEmpty @RequestBody List<@Valid RequestPaymentMethodDTO> requestPaymentMethodDTOs
    ) {
        List<PaymentMethodDTO> newPaymentMethods = paymentMethodUpdateService.createPaymentMethods(requestPaymentMethodDTOs);
        ResponseDTO<List<PaymentMethodDTO>> responseDTO = ResponseDTO.<List<PaymentMethodDTO>>builder()
                .message("Payment methods created successfully")
                .data(newPaymentMethods)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}