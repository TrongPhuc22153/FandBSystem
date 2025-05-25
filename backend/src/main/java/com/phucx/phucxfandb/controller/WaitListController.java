package com.phucx.phucxfandb.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.phucx.phucxfandb.constant.ValidationGroups;
import com.phucx.phucxfandb.constant.Views;
import com.phucx.phucxfandb.dto.request.WaitListRequestParamsDTO;
import com.phucx.phucxfandb.dto.request.RequestWaitListDTO;
import com.phucx.phucxfandb.dto.response.WaitListDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.waitlist.WaitListReaderService;
import com.phucx.phucxfandb.service.waitlist.WaitListUpdateService;
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
@Tag(name = "Wait list API", description = "Wait list endpoint")
@RequestMapping(value = "/api/v1/waitlists", produces = MediaType.APPLICATION_JSON_VALUE)
public class WaitListController {
    private final WaitListReaderService waitListReaderService;
    private final WaitListUpdateService waitListUpdateService;

    @GetMapping
    @Operation(summary = "Get all waitlist entries", description = "Public access")
    public ResponseEntity<Page<WaitListDTO>> getDineInWaitLists(@ModelAttribute WaitListRequestParamsDTO params){
        Page<WaitListDTO> data = waitListReaderService.getWaitLists(params);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get waitlist entry by ID", description = "Public access")
    public ResponseEntity<WaitListDTO> getDineInWaitList(
            @PathVariable(name = "id") String waitListId
    ){
        WaitListDTO data = waitListReaderService.getWaitList(waitListId);
        return ResponseEntity.ok().body(data);
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update waitlist entry", description = "Employee access")
    public ResponseEntity<ResponseDTO<WaitListDTO>> updateDineInWaitList(
            @Valid @RequestBody RequestWaitListDTO requestWaitListDTO,
            @PathVariable(name = "id") String waitListId
    ){
        WaitListDTO updatedWaitList = waitListUpdateService.updateWaitList(waitListId, requestWaitListDTO);
        ResponseDTO<WaitListDTO> responseDTO = ResponseDTO.<WaitListDTO>builder()
                .message("Waitlist entry updated successfully")
                .data(updatedWaitList)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new waitlist entry", description = "Employee access")
    public ResponseEntity<ResponseDTO<WaitListDTO>> createDineInWaitList(
            Authentication authentication,
            @Valid @RequestBody RequestWaitListDTO requestWaitListDTO
    ){
        WaitListDTO newWaitList = waitListUpdateService
                .createWaitList(authentication, requestWaitListDTO);
        ResponseDTO<WaitListDTO> responseDTO = ResponseDTO.<WaitListDTO>builder()
                .message("Waitlist entry created successfully")
                .data(newWaitList)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    @PatchMapping("{id}")
    @Operation(summary = "Update waitlist entry status", description = "Employee access")
    public ResponseEntity<ResponseDTO<WaitListDTO>> updateDineInWaitListStatus(
            @PathVariable String id,
            @JsonView(Views.UpdateWaitListStatus.class)
            @Validated(ValidationGroups.UpdateWaitListStatus.class)
            @RequestBody RequestWaitListDTO requestWaitListDTO
    ) {
        var data = waitListUpdateService.updateWaitListStatus(id, requestWaitListDTO);
        ResponseDTO<WaitListDTO> response = ResponseDTO.<WaitListDTO>builder()
                .message("Waitlist entry status updated successfully")
                .data(data)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
