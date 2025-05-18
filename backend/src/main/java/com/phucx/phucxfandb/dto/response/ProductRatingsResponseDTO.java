package com.phucx.phucxfandb.dto.response;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

@Value
@Builder
public class ProductRatingsResponseDTO {
    Page<RatingDTO> ratings;
    BigDecimal averageScore;
}
