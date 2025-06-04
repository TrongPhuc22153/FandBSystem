package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.response.RefundPreviewDTO;
import com.phucx.phucxfandb.service.refund.OrderRefundProcessorService;
import com.phucx.phucxfandb.service.refund.ReservationRefundProcessorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/refunds",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Refund API", description = "Refund operation endpoints")
public class RefundController {
    private final OrderRefundProcessorService orderRefundProcessorService;
    private final ReservationRefundProcessorService reservationRefundProcessorService;

    @GetMapping("/orders/{orderId}/preview")
    @Operation(summary = "Get order refund preview", description = "Authenticated access")
    public ResponseEntity<RefundPreviewDTO> getOrderRefundPreview(@PathVariable  String orderId) {
        RefundPreviewDTO refundPreview = orderRefundProcessorService.validateRefund(orderId);
        return ResponseEntity.ok(refundPreview);
    }

    @GetMapping("/reservations/{reservationId}/preview")
    @Operation(summary = "Get reservation refund preview", description = "Authenticated access")
    public ResponseEntity<RefundPreviewDTO> getReservationRefundPreview(@PathVariable  String reservationId) {
        RefundPreviewDTO refundPreview = reservationRefundProcessorService.validateRefund(reservationId);
        return ResponseEntity.ok(refundPreview);
    }


}
