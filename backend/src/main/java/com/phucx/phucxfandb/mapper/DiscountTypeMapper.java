package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestDiscountTypeDTO;
import com.phucx.phucxfandb.dto.response.DiscountTypeDTO;
import com.phucx.phucxfandb.entity.DiscountType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DiscountTypeMapper {

    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    DiscountType toDiscountType(RequestDiscountTypeDTO requestDiscountTypeDTO);

    DiscountTypeDTO toDiscountTypeDTO(DiscountType discountType);

    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateDiscountType(RequestDiscountTypeDTO requestDiscountTypeDTO, @MappingTarget DiscountType discountType);
}
