package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RatingRequestParamsDTO;
import com.phucx.phucxfandb.dto.request.RequestRatingDTO;
import com.phucx.phucxfandb.dto.response.ProductRatingsResponseDTO;
import com.phucx.phucxfandb.dto.response.RatingDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.rating.RatingReaderService;
import com.phucx.phucxfandb.service.rating.RatingUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/ratings", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Rating API", description = "Public and Customer operations for products")
public class RatingController {
    private final RatingReaderService ratingReaderService;
    private final RatingUpdateService ratingUpdateService;

    @GetMapping("/products/{productId}")
    @Operation(summary = "Get ratings for product", description = "Public access")
    public ResponseEntity<ProductRatingsResponseDTO> getRatingsByProductId(
            @ModelAttribute RatingRequestParamsDTO params,
            @PathVariable Long productId
    ) {
        ProductRatingsResponseDTO ratings = ratingReaderService
                .getRatingsAndAverageScoreByProductId(productId, params);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/products/{productId}/me")
    @Operation(summary = "Get user rating for product", description = "Customer access")
    public ResponseEntity<RatingDTO> getUserRatingProduct(
            Principal principal,
            @PathVariable Long productId
    ) {
        return ratingReaderService.getUserOptionalRating(principal.getName(), productId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create rating for product", description = "Customer access")
    public ResponseEntity<ResponseDTO<RatingDTO>> createRating(
            Principal principal,
            @RequestBody @Valid RequestRatingDTO requestRatingDTO) {
        RatingDTO ratingDTO = ratingUpdateService.createRating(principal.getName(), requestRatingDTO);
        ResponseDTO<RatingDTO> responseDTO = ResponseDTO.<RatingDTO>builder()
                .message("Your rating created successfully")
                .data(ratingDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @PutMapping(value = "/{ratingId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update rating for product", description = "Customer access")
    public ResponseEntity<ResponseDTO<RatingDTO>> updateRating(
            Principal principal,
            @PathVariable String ratingId,
            @RequestBody @Valid RequestRatingDTO requestRatingDTO) {
        RatingDTO ratingDTO = ratingUpdateService.updateRating(principal.getName(), ratingId, requestRatingDTO);
        ResponseDTO<RatingDTO> responseDTO = ResponseDTO.<RatingDTO>builder()
                .message("Your rating updated successfully")
                .data(ratingDTO)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @DeleteMapping("/{ratingId}")
    @Operation(summary = "Delete rating", description = "Customer access")
    public ResponseEntity<ResponseDTO<Void>> deleteRating(
            Principal principal,
            @PathVariable String ratingId) {
        ratingUpdateService.deleteRating(principal.getName(), ratingId);
        ResponseDTO<Void> responseDTO = ResponseDTO.<Void>builder()
                .message("Your rating deleted successfully")
                .build();
        return ResponseEntity.ok(responseDTO);
    }
}
