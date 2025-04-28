package com.phucx.phucxfandb.service.category;

import com.phucx.phucxfandb.dto.response.CategoryDTO;
import com.phucx.phucxfandb.entity.Category;
import org.springframework.data.domain.Page;

public interface CategoryReaderService {
    Page<CategoryDTO> getCategories(int pageNumber, int pageSize);
    CategoryDTO getCategory(long categoryID);
    CategoryDTO getCategory(String categoryName);
    Category getCategoryEntity(long categoryId);
} 
