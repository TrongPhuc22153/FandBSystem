package com.phucx.phucxfandb.service.rating;

import com.phucx.phucxfandb.dto.request.RatingRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.ProductRatingsResponseDTO;
import com.phucx.phucxfandb.dto.response.RatingDTO;

import java.util.Optional;

public interface RatingReaderService {

    ProductRatingsResponseDTO getRatingsAndAverageScoreByProductId(long productId, RatingRequestParamsDTO params);

    Optional<RatingDTO> getUserOptionalRating(String username, long productId);
}
