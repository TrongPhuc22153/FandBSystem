package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestPaymentMethodDTO;
import com.phucx.phucxfandb.dto.response.PaymentMethodDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.paymentMethod.PaymentMethodReaderService;
import com.phucx.phucxfandb.service.paymentMethod.PaymentMethodUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @Operation(summary = "Get all payment methods", description = "Public access")
    @GetMapping
    public ResponseEntity<Page<PaymentMethodDTO>> getPaymentMethods(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", defaultValue = "10") Integer pageSize
    ) {
        Page<PaymentMethodDTO> data = paymentMethodReaderService.getPaymentMethods(pageNumber, pageSize);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Get payment method by ID or name", description = "Public access")
    @GetMapping(value = "/payment-method")
    public ResponseEntity<PaymentMethodDTO> getPaymentMethod(
            @Parameter(description = "Payment method ID to retrieve a single payment method", required = false)
            @RequestParam(name = "id", required = false) String id,
            @Parameter(description = "Payment method name to retrieve a single payment method", required = false)
            @RequestParam(name = "name", required = false) String name
    ) {
        if (id != null && name != null && !name.trim().isEmpty()) {
            throw new IllegalArgumentException("Cannot provide both id and name");
        }
        if (id != null) {
            PaymentMethodDTO data = paymentMethodReaderService.getPaymentMethod(id);
            return ResponseEntity.ok().body(data);
        }
        if (name != null && !name.trim().isEmpty()) {
            PaymentMethodDTO data = paymentMethodReaderService.getPaymentMethod(name);
            return ResponseEntity.ok().body(data);
        }
        throw new IllegalArgumentException("Either id or name must be provided");
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
            @Valid @RequestBody List<RequestPaymentMethodDTO> requestPaymentMethodDTOs
    ) {
        List<PaymentMethodDTO> newPaymentMethods = paymentMethodUpdateService.createPaymentMethods(requestPaymentMethodDTOs);
        ResponseDTO<List<PaymentMethodDTO>> responseDTO = ResponseDTO.<List<PaymentMethodDTO>>builder()
                .message("Payment methods created successfully")
                .data(newPaymentMethods)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}