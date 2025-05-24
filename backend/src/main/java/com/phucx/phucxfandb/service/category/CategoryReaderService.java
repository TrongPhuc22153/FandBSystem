package com.phucx.phucxfandb.service.category;

import com.phucx.phucxfandb.dto.request.CategoryRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.CategoryDTO;
import com.phucx.phucxfandb.entity.Category;
import org.springframework.data.domain.Page;

public interface CategoryReaderService {
    Page<CategoryDTO> getCategories(CategoryRequestParamsDTO params);
    CategoryDTO getCategory(long categoryID, Boolean isDeleted);
    Category getCategoryEntity(long categoryId);
} 
