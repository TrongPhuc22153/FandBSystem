package com.phucx.phucxfandb.service.category;

import com.phucx.phucxfandb.dto.response.CategoryDTO;
import com.phucx.phucxfandb.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface CategoryReaderService {
    Page<CategoryDTO> getCategories(int pageNumber, int pageSize, String sortBy, Sort.Direction direction, Boolean isDeleted);
    CategoryDTO getCategory(long categoryID, Boolean isDeleted);
    Category getCategoryEntity(long categoryId);
} 
