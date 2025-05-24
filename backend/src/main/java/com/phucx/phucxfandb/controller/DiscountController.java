package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.DiscountRequestParamsDTO;
import com.phucx.phucxfandb.dto.request.RequestDiscountDTO;
import com.phucx.phucxfandb.dto.response.DiscountDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.discount.DiscountReaderService;
import com.phucx.phucxfandb.service.discount.DiscountUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Discounts", description = "Discount endpoint")
@RequestMapping(value = "/api/v1/discounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class DiscountController {
    private final DiscountReaderService discountReaderService;
    private final DiscountUpdateService discountUpdateService;

    @Operation(summary = "Add new discount", description = "Admin access")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<DiscountDTO>> createDiscount(
            @Valid @RequestBody RequestDiscountDTO requestDiscountDTO
    ){
        DiscountDTO newDiscount = discountUpdateService.createDiscount(requestDiscountDTO);
        ResponseDTO<DiscountDTO> responseDTO = ResponseDTO.<DiscountDTO>builder()
                .message("Discount created successfully")
                .data(newDiscount)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @Operation(summary = "Update discount", description = "Admin access")
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<DiscountDTO>> updateDiscount(
            @PathVariable String id,
            @Valid @RequestBody RequestDiscountDTO requestDiscountDTO
    ){
        DiscountDTO updatedDiscount = discountUpdateService.updateDiscount(id, requestDiscountDTO);
        ResponseDTO<DiscountDTO> responseDTO = ResponseDTO.<DiscountDTO>builder()
                .message("Discount updated successfully")
                .data(updatedDiscount)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }


    @GetMapping("/products/{id}")
    @Operation(summary = "Get discounts of a product", description = "Admin access")
    public ResponseEntity<Page<DiscountDTO>> getDiscountsByProductID(
            @PathVariable(name = "id") Long id,
            @ModelAttribute DiscountRequestParamsDTO params){
        Page<DiscountDTO> discounts = discountReaderService
                .getDiscountsByProductId(id, params);
        return ResponseEntity.ok().body(discounts);
    }

    @Operation(summary = "Get discount by ID", description = "Public access")
    @GetMapping("/{id}")
    public ResponseEntity<DiscountDTO> getDiscount(
            @PathVariable(name = "id") String id
    ){
        DiscountDTO discount = discountReaderService.getDiscount(id);
        return ResponseEntity.ok().body(discount);
    }
}
