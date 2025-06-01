package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.constant.ValidationGroups;
import com.phucx.phucxfandb.dto.request.AvailableTableRequestParamsDTO;
import com.phucx.phucxfandb.dto.request.RequestTableDTO;
import com.phucx.phucxfandb.dto.request.TableRequestParamsDTODTO;
import com.phucx.phucxfandb.dto.response.TableDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.table.TableReaderService;
import com.phucx.phucxfandb.service.table.TableUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/tables",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Tables API", description = "Public and Admin operations for tables")
public class TableController {
    private final TableReaderService tableReaderService;
    private final TableUpdateService tableUpdateService;

    @GetMapping
    @Operation(summary = "Get all tables", description = "Public access")
    public ResponseEntity<Page<TableDTO>> getTables(@ModelAttribute TableRequestParamsDTODTO params) {
        Page<TableDTO> data = tableReaderService
                .getTables(params);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/availability")
    @Operation(summary = "Get table availability", description = "Public access")
    public ResponseEntity<Page<TableDTO>> getTableAvailability(@ModelAttribute AvailableTableRequestParamsDTO params) {
        var tables = tableReaderService.getTableAvailability(params);
        return ResponseEntity.ok(tables);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get table by Id", description = "Public access")
    public ResponseEntity<TableDTO> getTable(@PathVariable String id) {
        TableDTO data = tableReaderService
                .getTable(id);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Update table status", description = "Employee access")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<TableDTO>> updateTableStatus(
            @Validated(ValidationGroups.UpdateTableStatus.class) @RequestBody RequestTableDTO requestTableDTO,
            @PathVariable String id
    ) {
        TableDTO updatedTable = tableUpdateService
                .updateTableStatus(id, requestTableDTO);
        ResponseDTO<TableDTO> responseDTO = ResponseDTO.<TableDTO>builder()
                .message("Table updated successfully")
                .data(updatedTable)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Operation(summary = "Update table", description = "Admin access")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<TableDTO>> updateTable(
            @Valid @RequestBody RequestTableDTO requestTableDTO,
            @PathVariable String id
    ) {
        TableDTO updatedTable = tableUpdateService
                .updateTable(id, requestTableDTO);
        ResponseDTO<TableDTO> responseDTO = ResponseDTO.<TableDTO>builder()
                .message("Table updated successfully")
                .data(updatedTable)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Operation(summary = "Create new table", description = "Admin access")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<TableDTO>> createTable(
            @Valid @RequestBody RequestTableDTO requestTableDTO
    ) {
        TableDTO newTable = tableUpdateService
                .createTable(requestTableDTO);
        ResponseDTO<TableDTO> responseDTO = ResponseDTO.<TableDTO>builder()
                .message("Table created successfully")
                .data(newTable)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(summary = "Create new tables", description = "Admin access")
    @PostMapping(value = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<List<TableDTO>>> createTables(
            @Valid @RequestBody List<RequestTableDTO> requestTableDTOS
    ) {
        List<TableDTO> newTables = tableUpdateService
                .createTables(requestTableDTOS);
        ResponseDTO<List<TableDTO>> responseDTO = ResponseDTO.<List<TableDTO>>builder()
                .message("Tables created successfully")
                .data(newTables)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}