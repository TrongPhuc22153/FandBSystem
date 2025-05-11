package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.request.ReservationRequestParamDTO;
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

import java.security.Principal;

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
            @ModelAttribute ReservationRequestParamDTO params
    ) {
        Page<ReservationDTO> reservations = reservationReaderService
                .getReservations(params, authentication);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{reservationId}")
    @Operation(summary = "Get reservation", description = "Authenticated access")
    public ResponseEntity<ReservationDTO> getReservation(
            @PathVariable(name = "reservationId") String reservationId
    ) {
        ReservationDTO reservation = reservationReaderService.getReservation(reservationId);
        return ResponseEntity.ok(reservation);
    }

    @PatchMapping("/{reservationId}")
    @Operation(summary = "Update reservation status", description = "Employee access")
    public ResponseEntity<ResponseDTO<ReservationDTO>> updateReservationStatus(
            Principal principal,
            @PathVariable String reservationId,
            @RequestBody RequestReservationDTO requestReservationDTO
    ) {
        log.info("updateReservationStatus(reservationId={})", reservationId);
        ReservationDTO updatedReservation = reservationProcessingService.processReservation(
                principal.getName(),
                reservationId,
                requestReservationDTO.getAction());
        ResponseDTO<ReservationDTO> response = ResponseDTO.<ReservationDTO>builder()
                .message("Reservation updated successfully")
                .data(updatedReservation)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new reservation", description = "Authenticated access")
    public ResponseEntity<ResponseDTO<ReservationDTO>> createReservation(
            Authentication authentication,
            @Valid @RequestBody RequestReservationDTO requestReservationDTO // Changed RequestOrderDTO
    ) {
        log.info("createReservation(requestReservationDTO={})", requestReservationDTO);
        ReservationDTO reservationDTO = reservationProcessingService.placeReservation(requestReservationDTO, authentication);
        ResponseDTO<ReservationDTO> response = ResponseDTO.<ReservationDTO>builder()
                .message("Reservation placed successfully")
                .data(reservationDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
