package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestProductDTO;
import com.phucx.phucxfandb.dto.response.ProductDTO;
import com.phucx.phucxfandb.entity.Category;
import com.phucx.phucxfandb.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toProductDTO(Product product);

    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "discounts", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "picture", source = "requestProductDTO.picture")
    @Mapping(target = "description", source = "requestProductDTO.description")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Product toProduct(RequestProductDTO requestProductDTO, Category category);

    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "discounts", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "picture", source = "requestProductDTO.picture")
    @Mapping(target = "description", source = "requestProductDTO.description")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateProductFromDTO(RequestProductDTO requestProductDTO, Category category, @MappingTarget  Product product);

}
