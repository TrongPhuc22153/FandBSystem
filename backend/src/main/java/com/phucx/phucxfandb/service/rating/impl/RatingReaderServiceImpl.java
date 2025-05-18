package com.phucx.phucxfandb.service.rating.impl;

import com.phucx.phucxfandb.constant.ProductRatingStatus;
import com.phucx.phucxfandb.dto.request.RatingRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.ProductRatingsResponseDTO;
import com.phucx.phucxfandb.dto.response.RatingDTO;
import com.phucx.phucxfandb.entity.Rating;
import com.phucx.phucxfandb.entity.UserProfile;
import com.phucx.phucxfandb.mapper.RatingMapper;
import com.phucx.phucxfandb.repository.RatingRepository;
import com.phucx.phucxfandb.service.image.ImageReaderService;
import com.phucx.phucxfandb.service.product.ProductReaderService;
import com.phucx.phucxfandb.service.rating.RatingReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingReaderServiceImpl implements RatingReaderService {
    private final ProductReaderService productReaderService;
    private final ImageReaderService imageReaderService;
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    @Override
    @Transactional(readOnly = true)
    public ProductRatingsResponseDTO getRatingsAndAverageScoreByProductId(long productId, RatingRequestParamsDTO params) {
        Pageable pageable = PageRequest.of(
                params.getPage(),
                params.getSize(),
                Sort.by(params.getDirection(), params.getField())
        );
        Page<RatingDTO> ratingDTOPage = ratingRepository.findByProductProductId(productId, pageable)
                .map(this::setUserProfileImage)
                .map(ratingMapper::toRatingDTO);
        BigDecimal averageScore = ratingRepository.findAverageRatingByProductId(productId);
        return ProductRatingsResponseDTO.builder()
                .ratings(ratingDTOPage)
                .averageScore(averageScore)
                .build();

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RatingDTO> getUserOptionalRating(String username, long productId) {
        ProductRatingStatus ratingStatus = productReaderService.getRatingStatus(username, productId);

        if (ratingStatus == ProductRatingStatus.NOT_PURCHASED) {
            return Optional.empty();
        }

        return productReaderService.getRatingProductEntity(username, productId)
                .flatMap(product -> product.getRatings().stream()
                        .filter(rating -> rating.getCustomer().getProfile().getUser().getUsername().equals(username))
                        .findFirst()
                        .map(ratingMapper::toRatingDTO)
                ).or(() -> Optional.of(RatingDTO.builder().build()));
    }

    private Rating setUserProfileImage(Rating rating){
        UserProfile profile = rating.getCustomer().getProfile();
        if(!(profile.getPicture()==null || profile.getPicture().isEmpty())){
            String imageUrl = imageReaderService.getImageUrl(profile.getPicture());
            profile.setPicture(imageUrl);
        }
        return rating;
    }
}
