package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestRatingDTO;
import com.phucx.phucxfandb.dto.response.RatingDTO;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.entity.Product;
import com.phucx.phucxfandb.entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface RatingMapper {
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "customer.profile.user", qualifiedByName = "toBriefUserDTO")
    RatingDTO toRatingDTO(Rating rating);

    @Named("productWithRatings")
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "customer", ignore = true)
    RatingDTO toProductRatingDTO(Rating rating);

    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "id", ignore = true)
    Rating toRating(RequestRatingDTO requestRatingDTO, Customer customer, Product product);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateRating(RequestRatingDTO requestRatingDTO, @MappingTarget Rating rating);

}
