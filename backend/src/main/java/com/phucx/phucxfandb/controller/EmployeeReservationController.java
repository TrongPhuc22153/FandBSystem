package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.reservation.ReservationProcessingService;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/v1/employee/reservations")
@RequiredArgsConstructor
@Tag(name = "Employee Reservation API", description = "Endpoints for employee operations (waiter, receptionist, chef) in the F&B reservation flow")
public class EmployeeReservationController {
    private final ReservationReaderService reservationReaderService;
    private final ReservationProcessingService reservationProcessingService;

    @GetMapping
    @Operation(summary = "Retrieve all employee reservations", description = "Gets a list of all employee reservations.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reservations"),
    })
    public ResponseEntity<Page<ReservationDTO>> getAllEmployeeReservations(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", defaultValue = "10") Integer pageSize
    ) {
        Page<ReservationDTO> reservations = reservationReaderService.getAllReservations(pageNumber, pageSize);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{reservationId}")
    @Operation(summary = "Retrieve an employee reservation by ID", description = "Gets a single employee reservation by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reservation"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<ResponseDTO<ReservationDTO>> getEmployeeReservationById(
            @Parameter(description = "ID of the employee reservation to retrieve", required = true) @PathVariable String reservationId) {
        ReservationDTO reservation = reservationReaderService.getReservation(reservationId);
        ResponseDTO<ReservationDTO> response = ResponseDTO.<ReservationDTO>builder()
                .message("Employee reservation retrieved successfully")
                .data(reservation)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{reservationId}/confirm")
    @Operation(summary = "Confirm a reservation", description = "Allows an employee to confirm a reservation.")
    public ResponseEntity<ResponseDTO<ReservationDTO>> confirmReservation(
            Principal principal,
            @PathVariable String reservationId) {
        log.info("confirmReservation(username={}, reservationId={})", principal.getName(), reservationId);
        ReservationDTO reservationDTO = reservationProcessingService.confirmReservation(principal.getName(), reservationId);
        ResponseDTO<ReservationDTO> response = ResponseDTO.<ReservationDTO>builder()
                .message("Reservation confirmed")
                .data(reservationDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{reservationId}/cancel")
    @Operation(summary = "Cancel a reservation", description = "Allows an employee to cancel a reservation.")
    public ResponseEntity<ResponseDTO<Void>> cancelReservation(
            Principal principal,
            @PathVariable String reservationId) {
        log.info("cancelReservation(username={}, reservationId={})", principal.getName(), reservationId);
        reservationProcessingService.cancelReservation(principal.getName(), reservationId);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message("Reservation cancelled")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{reservationId}/receive")
    @Operation(summary = "Mark a reservation as received", description = "Allows an employee to mark a reservation as received.")
    public ResponseEntity<ResponseDTO<ReservationDTO>> markReservationAsReceived(
            Principal principal,
            @PathVariable String reservationId) {
        log.info("markReservationAsReceived(username={}, reservationId={})", principal.getName(), reservationId);
        ReservationDTO reservationDTO = reservationProcessingService.markReservationAsReceived(principal.getName(), reservationId);
        ResponseDTO<ReservationDTO> response = ResponseDTO.<ReservationDTO>builder()
                .message("Reservation marked as received")
                .data(reservationDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/employees/{username}") // Corrected path for creating employee reservations
    @Operation(summary = "Create a reservation for an employee", description = "Allows an employee to create a reservation for another employee.")
    public ResponseEntity<ResponseDTO<ReservationDTO>> createEmployeeReservation(
            @PathVariable String username, // Changed to PathVariable
            @Valid @RequestBody RequestReservationDTO requestReservationDTO) { // Added RequestBody
        log.info("createEmployeeReservation(username={}, requestReservationDTO={})", username, requestReservationDTO);
        ReservationDTO reservationDTO = reservationProcessingService.placeEmployeeReservation(username, requestReservationDTO);
        ResponseDTO<ReservationDTO> response = ResponseDTO.<ReservationDTO>builder()
                .message("Employee reservation created")
                .data(reservationDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/{reservationId}/complete")
    @Operation(summary = "Complete a reservation", description = "Allows an employee to complete a reservation.")
    public ResponseEntity<ResponseDTO<ReservationDTO>> completeReservation(
            Principal principal,
            @PathVariable String reservationId) {
        log.info("completeReservation(username={}, reservationId={})", principal.getName(), reservationId);
        ReservationDTO reservationDTO = reservationProcessingService.completeReservation(principal.getName(), reservationId);
        ResponseDTO<ReservationDTO> response = ResponseDTO.<ReservationDTO>builder()
                .message("Reservation completed")
                .data(reservationDTO)
                .build();
        return ResponseEntity.ok(response);
    }
}
