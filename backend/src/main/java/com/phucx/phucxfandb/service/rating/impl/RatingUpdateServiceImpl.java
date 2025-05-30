package com.phucx.phucxfandb.service.rating.impl;

import com.phucx.phucxfandb.enums.ProductRatingStatus;
import com.phucx.phucxfandb.dto.request.RequestRatingDTO;
import com.phucx.phucxfandb.dto.response.RatingDTO;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.entity.Product;
import com.phucx.phucxfandb.entity.Rating;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.RatingMapper;
import com.phucx.phucxfandb.repository.RatingRepository;
import com.phucx.phucxfandb.service.customer.CustomerReaderService;
import com.phucx.phucxfandb.service.product.ProductReaderService;
import com.phucx.phucxfandb.service.rating.RatingUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingUpdateServiceImpl implements RatingUpdateService {
    private final CustomerReaderService customerReaderService;
    private final ProductReaderService productReaderService;
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    @Override
    @Transactional
    public RatingDTO updateRating(String username, String ratingId, RequestRatingDTO requestRatingDTO) {
        Rating existingRating = ratingRepository.findByIdAndCustomerProfileUserUsername(ratingId, username)
                .orElseThrow(() -> new NotFoundException(Rating.class.getSimpleName(), "id", ratingId));

        ratingMapper.updateRating(requestRatingDTO, existingRating);
        Rating updatedRating = ratingRepository.save(existingRating);
        return ratingMapper.toRatingDTO(updatedRating);
    }

    @Override
    @Transactional
    public RatingDTO createRating(String username, RequestRatingDTO requestRatingDTO) {
        ProductRatingStatus status = productReaderService.getRatingStatus(username, requestRatingDTO.getProductId());
        if(status.equals(ProductRatingStatus.NOT_PURCHASED)){
            throw new AccessDeniedException("You is not allowed to rate a product you haven't purchased.");
        }
        return ratingRepository.findByProductProductIdAndCustomerProfileUserUsername(requestRatingDTO.getProductId(), username)
                .map(rating -> updateExistingRating(rating, requestRatingDTO))
                .orElseGet(() -> createNewRating(username, requestRatingDTO));
    }

    private RatingDTO updateExistingRating(Rating rating, RequestRatingDTO dto) {
        ratingMapper.updateRating(dto, rating);
        return ratingMapper.toRatingDTO(ratingRepository.save(rating));
    }

    private RatingDTO createNewRating(String username, RequestRatingDTO dto) {
        Product product = productReaderService.getProductEntity(dto.getProductId());
        Customer customer = customerReaderService.getCustomerEntityByUsername(username);
        Rating newRating = ratingMapper.toRating(dto, customer, product);
        return ratingMapper.toRatingDTO(ratingRepository.save(newRating));
    }

    @Override
    @Transactional
    public void deleteRating(String username, String ratingId) {
        Rating existingRating = ratingRepository.findByIdAndCustomerProfileUserUsername(ratingId, username)
                .orElseThrow(() -> new NotFoundException(Rating.class.getSimpleName(), "id", ratingId));
        ratingRepository.delete(existingRating);
    }
}
