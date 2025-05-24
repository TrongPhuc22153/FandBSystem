package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.request.ReservationRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.PaymentProcessingDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.reservation.ReservationProcessingService;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation API", description = "Endpoints for reservations")
public class ReservationController {
    private final ReservationReaderService reservationReaderService;
    private final ReservationProcessingService reservationProcessingService;

    @GetMapping
    @Operation(summary = "Get reservations", description = "Authenticated access")
    public ResponseEntity<Page<ReservationDTO>> getReservations(
            Authentication authentication,
            @ModelAttribute ReservationRequestParamsDTO params
    ) {
        Page<ReservationDTO> reservations = reservationReaderService
                .getReservations(params, authentication);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reservation", description = "Authenticated access")
    public ResponseEntity<ReservationDTO> getReservation(
            @PathVariable(name = "id") String id
    ) {
        ReservationDTO reservation = reservationReaderService.getReservation(id);
        return ResponseEntity.ok(reservation);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Process reservation", description = "Employee access")
    public ResponseEntity<ResponseDTO<ReservationDTO>> processReservation(
            Authentication authentication,
            @PathVariable String id,
            @RequestBody RequestReservationDTO requestReservationDTO
    ) {
        ReservationDTO updatedReservation = reservationProcessingService.processReservation(
                authentication,
                id,
                requestReservationDTO.getAction());
        ResponseDTO<ReservationDTO> response = ResponseDTO.<ReservationDTO>builder()
                .message("Reservation updated successfully")
                .data(updatedReservation)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new reservation", description = "Authenticated access")
    public ResponseEntity<ResponseDTO<PaymentProcessingDTO>> createReservation(
            Authentication authentication,
            @Valid @RequestBody RequestReservationDTO requestReservationDTO
    ) {
        PaymentProcessingDTO paymentProcessingDTO = reservationProcessingService.placeReservation(requestReservationDTO, authentication);
        ResponseDTO<PaymentProcessingDTO> response = ResponseDTO.<PaymentProcessingDTO>builder()
                .message("Reservation placed successfully")
                .data(paymentProcessingDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
