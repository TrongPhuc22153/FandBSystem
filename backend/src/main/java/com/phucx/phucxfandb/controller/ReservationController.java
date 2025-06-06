package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.constant.ValidationGroups;
import com.phucx.phucxfandb.dto.request.RequestMenuItemDTO;
import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.request.ReservationRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.reservation.MenuItemService;
import com.phucx.phucxfandb.service.reservation.ReservationProcessingService;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import com.phucx.phucxfandb.service.reservation.ReservationUpdateService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation API", description = "Endpoints for reservations")
public class ReservationController {
    private final ReservationReaderService reservationReaderService;
    private final ReservationUpdateService reservationUpdateService;
    private final MenuItemService menuItemService;
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
    public ResponseEntity<ResponseDTO<ReservationDTO>> createReservation(
            Authentication authentication,
            @Valid @RequestBody RequestReservationDTO requestReservationDTO
    ) {
        ReservationDTO reservation = reservationProcessingService
                .placeReservation(requestReservationDTO, authentication);
        ResponseDTO<ReservationDTO> response = ResponseDTO.<ReservationDTO>builder()
                .message("Reservation placed successfully")
                .data(reservation)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update reservation", description = "Authenticated access")
    public ResponseEntity<ResponseDTO<ReservationDTO>> updateReservation(
            @PathVariable String id,
            Authentication authentication,
            @Valid @RequestBody RequestReservationDTO requestReservationDTO
    ) {
        ReservationDTO reservation = reservationUpdateService
                .updateReservation(authentication.getName(), id, requestReservationDTO);

        ResponseDTO<ReservationDTO> response = ResponseDTO.<ReservationDTO>builder()
                .message("Reservation updated successfully")
                .data(reservation)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/{id}/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add reservation item", description = "Employee access")
    public ResponseEntity<ResponseDTO<ReservationDTO>> addReservationItem(
            @PathVariable String id,
            @RequestBody RequestMenuItemDTO request) {
        ReservationDTO updatedReservation = menuItemService.addItem(id, request);

        ResponseDTO<ReservationDTO> responseDTO = ResponseDTO.<ReservationDTO>builder()
                .message("Reservation item added successfully")
                .data(updatedReservation)
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{reservationId}/items/{itemId}")
    @Operation(summary = "Update reservation item quantity", description = "Employee access")
    public ResponseEntity<ResponseDTO<ReservationDTO>> updateReservationItemQuantity(
            @PathVariable String reservationId,
            @PathVariable String itemId,
            @RequestBody RequestMenuItemDTO request) {
        ReservationDTO updatedReservation = menuItemService.updateItemQuantity(reservationId, itemId, request);

        ResponseDTO<ReservationDTO> responseDTO = ResponseDTO.<ReservationDTO>builder()
                .message("Reservation item quantity updated successfully")
                .data(updatedReservation)
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{reservationId}/items/{itemId}")
    @Operation(summary = "Update reservation item status", description = "Employee access")
    public ResponseEntity<ResponseDTO<ReservationDTO>> updateReservationItemStatus(
            @PathVariable String reservationId,
            @PathVariable String itemId,
            @Validated(ValidationGroups.UpdateReservationItemStatus.class)
            @RequestBody RequestMenuItemDTO request) {
        ReservationDTO updatedReservation = menuItemService.updateItemStatus(reservationId, itemId, request);

        ResponseDTO<ReservationDTO> responseDTO = ResponseDTO.<ReservationDTO>builder()
                .message("Reservation item status updated successfully")
                .data(updatedReservation)
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{reservationId}/items/{itemId}")
    @Operation(summary = "Cancel reservation item", description = "Employee access")
    public ResponseEntity<ResponseDTO<Void>> cancelReservationItem(
            @PathVariable String reservationId,
            @PathVariable String itemId){
        menuItemService.cancelItem(reservationId, itemId);
        ResponseDTO<Void> responseDTO = ResponseDTO.<Void>builder()
                .message("Reservation item cancelled successfully")
                .build();

        return ResponseEntity.ok(responseDTO);
    }

}
