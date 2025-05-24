package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.DiscountTypeParamsDTO;
import com.phucx.phucxfandb.dto.request.RequestDiscountTypeDTO;
import com.phucx.phucxfandb.dto.response.DiscountTypeDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.discountType.DiscountTypeReaderService;
import com.phucx.phucxfandb.service.discountType.DiscountTypeUpdateService;
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
@RequestMapping(value = "/api/v1/discount-types",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Discount Types", description = "Admin operations for discount types")
public class DiscountTypeController {
    private final DiscountTypeReaderService discountTypeReaderService;
    private final DiscountTypeUpdateService discountTypeUpdateService;

    @GetMapping
    @Operation(summary = "Get all discount types", description = "Public access")
    public ResponseEntity<Page<DiscountTypeDTO>> getDiscountTypes(@ModelAttribute DiscountTypeParamsDTO params) {
        Page<DiscountTypeDTO> data = discountTypeReaderService.getDiscountTypes(params);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Get discount type by Id", description = "Public access")
    @GetMapping(value = "/{id}")
    public ResponseEntity<DiscountTypeDTO> getDiscountType(@PathVariable Long id) {
        DiscountTypeDTO data = discountTypeReaderService.getDiscountType(id);
        return ResponseEntity.ok().body(data);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update discount type", description = "Admin access")
    public ResponseEntity<ResponseDTO<DiscountTypeDTO>> updateDiscountType(
            @Valid @RequestBody RequestDiscountTypeDTO requestDiscountTypeDTO,
            @PathVariable Long id
    ) {
        DiscountTypeDTO updatedDiscountType = discountTypeUpdateService.updateDiscountType(id, requestDiscountTypeDTO);
        ResponseDTO<DiscountTypeDTO> responseDTO = ResponseDTO.<DiscountTypeDTO>builder()
                .message("Discounts updated successfully")
                .data(updatedDiscountType)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new discount type", description = "Admin access")
    public ResponseEntity<ResponseDTO<DiscountTypeDTO>> createDiscountType(
            @Valid @RequestBody RequestDiscountTypeDTO requestDiscountTypeDTO
    ) {
        DiscountTypeDTO newDiscountType = discountTypeUpdateService.createDiscountType(requestDiscountTypeDTO);
        ResponseDTO<DiscountTypeDTO> responseDTO = ResponseDTO.<DiscountTypeDTO>builder()
                .message("Discount created successfully")
                .data(newDiscountType)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping(value = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new discount types", description = "Admin access")
    public ResponseEntity<ResponseDTO<List<DiscountTypeDTO>>> createDiscountTypes(
            @Valid @RequestBody List<@Valid RequestDiscountTypeDTO> requestDiscountTypeDTOs
    ) {
        List<DiscountTypeDTO> newDiscountTypes = discountTypeUpdateService.createDiscountTypes(requestDiscountTypeDTOs);
        ResponseDTO<List<DiscountTypeDTO>> responseDTO = ResponseDTO.<List<DiscountTypeDTO>>builder()
                .message("Discounts created successfully")
                .data(newDiscountTypes)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
