package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.constant.OrderStatus;
import com.phucx.phucxfandb.constant.ReservationStatus;
import com.phucx.phucxfandb.dto.response.OrderDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.order.OrderProcessingService;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import com.phucx.phucxfandb.service.reservation.ReservationProcessingService;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "KitChen API", description = "KitChen")
@RequestMapping(value = "/api/v1/kitchen", produces = MediaType.APPLICATION_JSON_VALUE)
public class KitChenController {

    private final OrderReaderService orderReaderService;
    private final OrderProcessingService orderProcessingService;
    private final ReservationProcessingService reservationProcessingService;
    private final ReservationReaderService reservationReaderService;


    @GetMapping("/orders-for-preparation")
    @Operation(summary = "Get orders for preparation", description = "Allows kitchen staff to retrieve orders that are ready for preparation.")
    public ResponseEntity<Page<OrderDTO>> getOrdersForPreparation(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info("getOrdersForPreparation()");
        Page<OrderDTO> orders = orderReaderService.getOrders(OrderStatus.CONFIRMED, page, size);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/reservations-for-preparation")
    @Operation(summary = "Get reservations for preparation", description = "Allows kitchen staff to retrieve reservations that are ready for preparation.")
    public ResponseEntity<Page<ReservationDTO>> getReservationsForPreparation(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info("getReservationsForPreparation()");
        Page<ReservationDTO> reservations = reservationReaderService.getReservations(ReservationStatus.PENDING, page, size);
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("/orders/{orderId}/prepared")
    @Operation(summary = "Mark an order as prepared", description = "Allows kitchen staff to mark an order as prepared.")
    public ResponseEntity<ResponseDTO<OrderDTO>> markOrderAsPrepared(
            @PathVariable String orderId
    ) {
        log.info("markOrderAsPrepared(orderId={})", orderId);
        OrderDTO updatedOrder = orderProcessingService.markOrderAsPrepared(orderId);
        ResponseDTO<OrderDTO> response = ResponseDTO.<OrderDTO>builder()
                .message("Order marked as prepared")
                .data(updatedOrder)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reservations/{reservationId}/prepared")
    @Operation(summary = "Mark an reservation as prepared", description = "Allows kitchen staff to mark an reservation as prepared.")
    public ResponseEntity<ResponseDTO<ReservationDTO>> markReservationAsPrepared(
            @PathVariable String reservationId
    ) {
        log.info("markReservationAsPrepared(reservationId={})", reservationId);
        ReservationDTO updatedReservation = reservationProcessingService.markReservationAsPrepared(reservationId);
        ResponseDTO<ReservationDTO> response = ResponseDTO.<ReservationDTO>builder()
                .message("Reservation marked as prepared")
                .data(updatedReservation)
                .build();
        return ResponseEntity.ok(response);
    }

}
