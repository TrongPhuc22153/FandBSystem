package com.phucx.phucxfandb.service.rating;

import com.phucx.phucxfandb.dto.request.RequestRatingDTO;
import com.phucx.phucxfandb.dto.response.RatingDTO;

public interface RatingUpdateService {
    RatingDTO updateRating(String username, String ratingId, RequestRatingDTO requestRatingDTO);
    RatingDTO createRating(String username, RequestRatingDTO requestRatingDTO);
    void deleteRating(String username, String ratingId);
}
