package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestProductSizeDTO;
import com.phucx.phucxfandb.dto.response.ProductSizeDTO;
import com.phucx.phucxfandb.entity.Product;
import com.phucx.phucxfandb.entity.ProductSize;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductSizeMapper {
    ProductSizeDTO toProductSizeDTO(ProductSize productSize);

    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "product", source = "product")
    ProductSize toProductSize(RequestProductSizeDTO requestProductSizeDTO, Product product);

    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    void updateProductSizeFromDTO(RequestProductSizeDTO requestProductSizeDTO, @MappingTarget ProductSize productSize);
}
