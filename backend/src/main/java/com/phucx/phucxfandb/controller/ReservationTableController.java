package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestReservationTableDTO;
import com.phucx.phucxfandb.dto.request.TableRequestParamDTO;
import com.phucx.phucxfandb.dto.response.ReservationTableDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.table.ReservationTableReaderService;
import com.phucx.phucxfandb.service.table.ReservationTableUpdateService;
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
@RequestMapping(value = "/api/v1/tables",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reservation Tables", description = "Public and Admin operations for reservation tables")
public class ReservationTableController {
    private final ReservationTableReaderService reservationTableReaderService;
    private final ReservationTableUpdateService reservationTableUpdateService;

    @Operation(summary = "Get all reservation tables", description = "Public access")
    @GetMapping
    public ResponseEntity<Page<ReservationTableDTO>> getReservationTables(@ModelAttribute TableRequestParamDTO params) {
        Page<ReservationTableDTO> data = reservationTableReaderService
                .getReservationTables(params);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Get reservation table by ID or name", description = "Public access")
    @GetMapping(value = "/table")
    public ResponseEntity<ReservationTableDTO> getReservationTable(
            @Parameter(description = "Table ID to retrieve a single reservation table", required = false)
            @RequestParam(name = "id", required = false) String id,
            @Parameter(description = "Table name to retrieve a single reservation table", required = false)
            @RequestParam(name = "name", required = false) String name
    ) {
        if (id != null && name != null && !name.trim().isEmpty()) {
            throw new IllegalArgumentException("Cannot provide both id and name");
        }
        if (id != null) {
            ReservationTableDTO data = reservationTableReaderService
                    .getReservationTable(id);
            return ResponseEntity.ok().body(data);
        }
        if (name != null && !name.trim().isEmpty()) {
            ReservationTableDTO data = reservationTableReaderService
                    .getReservationTable(name);
            return ResponseEntity.ok().body(data);
        }
        throw new IllegalArgumentException("Either id or name must be provided");
    }

    @Operation(summary = "Update reservation table", description = "Admin access")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<ReservationTableDTO>> updateReservationTable(
            @Valid @RequestBody RequestReservationTableDTO requestReservationTableDTO,
            @PathVariable String id
    ) {
        ReservationTableDTO updatedReservationTable = reservationTableUpdateService
                .updateReservationTable(id, requestReservationTableDTO);
        ResponseDTO<ReservationTableDTO> responseDTO = ResponseDTO.<ReservationTableDTO>builder()
                .message("Table updated successfully")
                .data(updatedReservationTable)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Operation(summary = "Update reservation table status", description = "Employee access")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<ReservationTableDTO>> updateReservationTableStatus(
            @RequestBody RequestReservationTableDTO requestReservationTableDTO,
            @PathVariable String id
    ) {
        ReservationTableDTO updatedReservationTable = reservationTableUpdateService
                .updateTableStatus(id, requestReservationTableDTO);
        ResponseDTO<ReservationTableDTO> responseDTO = ResponseDTO.<ReservationTableDTO>builder()
                .message("Table updated successfully")
                .data(updatedReservationTable)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Operation(summary = "Create new reservation table", description = "Admin access")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<ReservationTableDTO>> createReservationTable(
            @Valid @RequestBody RequestReservationTableDTO requestReservationTableDTO
    ) {
        ReservationTableDTO newReservationTable = reservationTableUpdateService
                .createReservationTable(requestReservationTableDTO);
        ResponseDTO<ReservationTableDTO> responseDTO = ResponseDTO.<ReservationTableDTO>builder()
                .message("Table created successfully")
                .data(newReservationTable)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(summary = "Create new reservation tables", description = "Admin access")
    @PostMapping(value = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<List<ReservationTableDTO>>> createReservationTables(
            @Valid @RequestBody List<RequestReservationTableDTO> requestReservationTableDTOs
    ) {
        List<ReservationTableDTO> newReservationTables = reservationTableUpdateService
                .createReservationTables(requestReservationTableDTOs);
        ResponseDTO<List<ReservationTableDTO>> responseDTO = ResponseDTO.<List<ReservationTableDTO>>builder()
                .message("Tables created successfully")
                .data(newReservationTables)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}