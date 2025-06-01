package com.phucx.phucxfandb.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.phucx.phucxfandb.constant.ValidationGroups;
import com.phucx.phucxfandb.constant.Views;
import com.phucx.phucxfandb.dto.request.TableOccupancyRequestParamsDTO;
import com.phucx.phucxfandb.dto.request.RequestTableOccupancyDTO;
import com.phucx.phucxfandb.dto.response.TableOccupancyDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.table.TableOccupancyReaderService;
import com.phucx.phucxfandb.service.table.TableOccupancyUpdateService;
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
@RequiredArgsConstructor
@Tag(name = "Table occupancy API", description = "Table occupancy endpoint")
@RequestMapping(value = "/api/v1/table-occupancies", produces = MediaType.APPLICATION_JSON_VALUE)
public class TableOccupancyController {
    private final TableOccupancyReaderService tableOccupancyReaderService;
    private final TableOccupancyUpdateService tableOccupancyUpdateService;

    @GetMapping
    @Operation(summary = "Get all table occupancies", description = "Public access")
    public ResponseEntity<Page<TableOccupancyDTO>> getTableOccupancies(@ModelAttribute TableOccupancyRequestParamsDTO params){
        Page<TableOccupancyDTO> data = tableOccupancyReaderService.getTableOccupancies(params);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get table occupancy entry by ID", description = "Public access")
    public ResponseEntity<TableOccupancyDTO> getTableOccupancy(
            @PathVariable(name = "id") String id
    ){
        TableOccupancyDTO data = tableOccupancyReaderService.getTableOccupancy(id);
        return ResponseEntity.ok().body(data);
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update table occupancy entry", description = "Employee access")
    public ResponseEntity<ResponseDTO<TableOccupancyDTO>> updateTableOccupancy(
            @Valid @RequestBody RequestTableOccupancyDTO requestTableOccupancyDTO,
            @PathVariable(name = "id") String id
    ){
        TableOccupancyDTO updated = tableOccupancyUpdateService.updateTableOccupancy(id, requestTableOccupancyDTO);
        ResponseDTO<TableOccupancyDTO> responseDTO = ResponseDTO.<TableOccupancyDTO>builder()
                .message("Table occupancy entry updated successfully")
                .data(updated)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new table occupancy entry", description = "Employee access")
    public ResponseEntity<ResponseDTO<TableOccupancyDTO>> createTableOccupancy(
            Authentication authentication,
            @Valid @RequestBody RequestTableOccupancyDTO requestTableOccupancyDTO
    ){
        TableOccupancyDTO saved = tableOccupancyUpdateService
                .createTableOccupancy(authentication, requestTableOccupancyDTO);
        ResponseDTO<TableOccupancyDTO> responseDTO = ResponseDTO.<TableOccupancyDTO>builder()
                .message("Table occupancy entry created successfully")
                .data(saved)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    @PatchMapping("{id}")
    @Operation(summary = "Update table occupancy entry status", description = "Employee access")
    public ResponseEntity<ResponseDTO<TableOccupancyDTO>> updateTableOccupancyStatus(
            @PathVariable String id,
            @JsonView(Views.UpdateTableOccupancyStatus.class)
            @Validated(ValidationGroups.UpdateTableOccupancyStatus.class)
            @RequestBody RequestTableOccupancyDTO requestTableOccupancyDTO
    ) {
        var data = tableOccupancyUpdateService.updateTableOccupancyStatus(id, requestTableOccupancyDTO);
        ResponseDTO<TableOccupancyDTO> response = ResponseDTO.<TableOccupancyDTO>builder()
                .message("Table occupancy entry status updated successfully")
                .data(data)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
