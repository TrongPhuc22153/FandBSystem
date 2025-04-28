package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestDiscountDTO;
import com.phucx.phucxfandb.dto.response.DiscountDTO;
import com.phucx.phucxfandb.entity.Discount;
import com.phucx.phucxfandb.entity.DiscountType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    DiscountDTO toDiscountDTO(Discount discount);

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "discountType", source = "discountType")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Discount toDiscount(RequestDiscountDTO requestDiscountDTO, DiscountType discountType);

    @Mapping(target = "discountId", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "discountType", source = "discountType")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateDiscount(RequestDiscountDTO requestDiscountDTO, DiscountType discountType, @MappingTarget Discount discount);

}
