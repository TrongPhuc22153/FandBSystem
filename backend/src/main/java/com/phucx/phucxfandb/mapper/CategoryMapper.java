package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestCategoryDTO;
import com.phucx.phucxfandb.dto.response.CategoryDTO;
import com.phucx.phucxfandb.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toCategoryDTO(Category category);

    @Named("categoryListEntry")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "picture", ignore = true)
    CategoryDTO toCategoryListEntry(Category category);

    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "categoryId", ignore = true)
    Category toCategory(RequestCategoryDTO requestCategoryDTO);

    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "categoryId", ignore = true)
    void updateCategory(RequestCategoryDTO requestCategoryDTO, @MappingTarget Category category);
}
