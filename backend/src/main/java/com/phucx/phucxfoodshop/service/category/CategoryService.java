package com.phucx.phucxfoodshop.service.category;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.phucxfoodshop.exceptions.EntityExistsException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.Category;

public interface CategoryService {
    public List<Category> getCategories();
    public Page<Category> getCategories(int pageNumber, int pageSize);
    public Category getCategory(int categoryID) throws NotFoundException;
    public Category getCategory(String categoryName) throws NotFoundException;
    public List<Category> getCategoryLike(String categoryName);
    // update 
    Category updateCategory(Category category) throws NotFoundException;
    // create
    Boolean createCategory(Category category) throws EntityExistsException;
} 
