package com.phucx.phucxfandb.service.category;

import com.phucx.phucxfandb.dto.request.RequestCategoryDTO;
import com.phucx.phucxfandb.dto.response.CategoryDTO;

import java.util.List;

public interface CategoryUpdateService {
    // update
    CategoryDTO updateCategory(Long categoryId, RequestCategoryDTO requestCategoryDTO);
    // create
    CategoryDTO createCategory(RequestCategoryDTO createCategoryDTO);
    List<CategoryDTO> createCategories(List<RequestCategoryDTO> createCategoryDTOs);
}
