package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.response.*;
import com.phucx.phucxfandb.service.reservation.ReservationProcessingService;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/customer/reservations")
@RequiredArgsConstructor
@Tag(name = "Customer Reservation API", description = "Customer reservation endpoints")
@SecurityRequirement(name = "bearerAuth")
public class CustomerReservationController {
    private final ReservationProcessingService reservationProcessingService;
    private final ReservationReaderService reservationReaderService;

    @GetMapping
    @Operation(summary = "Retrieve all customer reservations", description = "Gets a list of all customer reservations.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reservations"),
    })
    public ResponseEntity<Page<ReservationDTO>> getAllCustomerReservations(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", defaultValue = "10") Integer pageSize
    ) {
        Page<ReservationDTO> reservations = reservationReaderService.getAllReservations(pageNumber, pageSize);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{reservationId}")
    @Operation(summary = "Retrieve a customer reservation by ID", description = "Gets a single customer reservation by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reservation"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<ReservationDTO> getCustomerReservationById(
            @Parameter(description = "ID of the customer reservation to retrieve", required = true) @PathVariable String reservationId) {
        ReservationDTO reservation = reservationReaderService.getReservation(reservationId);
        return ResponseEntity.ok(reservation);
    }

    @PostMapping
    @Operation(summary = "Create a new customer reservation", description = "Creates a new reservation for a customer.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer reservation created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request - invalid input")
    })
    public ResponseEntity<ResponseDTO<ReservationDTO>> createCustomerReservation(
            Principal principal,
            @RequestBody RequestReservationDTO createReservationRequest) {
        ReservationDTO createdReservation = reservationProcessingService.placeCustomerReservation(
                principal.getName(),
                createReservationRequest);
        ResponseDTO<ReservationDTO> response = ResponseDTO.<ReservationDTO>builder()
                .message("Customer reservation created successfully")
                .data(createdReservation)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
