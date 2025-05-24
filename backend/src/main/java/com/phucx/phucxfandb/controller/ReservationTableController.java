package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestReservationTableDTO;
import com.phucx.phucxfandb.dto.request.TableRequestParamsDTODTO;
import com.phucx.phucxfandb.dto.response.ReservationTableDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.table.ReservationTableReaderService;
import com.phucx.phucxfandb.service.table.ReservationTableUpdateService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Reservation Tables", description = "Public and Admin operations for tables")
public class ReservationTableController {
    private final ReservationTableReaderService reservationTableReaderService;
    private final ReservationTableUpdateService reservationTableUpdateService;

    @GetMapping
    @Operation(summary = "Get all tables", description = "Public access")
    public ResponseEntity<Page<ReservationTableDTO>> getTables(@ModelAttribute TableRequestParamsDTODTO params) {
        Page<ReservationTableDTO> data = reservationTableReaderService
                .getReservationTables(params);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get table by Id", description = "Public access")
    public ResponseEntity<ReservationTableDTO> getTable(@PathVariable String id) {
        ReservationTableDTO data = reservationTableReaderService
                .getReservationTable(id);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Update table status", description = "Employee access")
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

    @Operation(summary = "Update table", description = "Admin access")
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

    @Operation(summary = "Create new table", description = "Admin access")
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

    @Operation(summary = "Create new tables", description = "Admin access")
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