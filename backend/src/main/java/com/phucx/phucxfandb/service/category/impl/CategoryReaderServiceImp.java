package com.phucx.phucxfandb.service.category.impl;

import com.phucx.phucxfandb.dto.response.CategoryDTO;
import com.phucx.phucxfandb.entity.Category;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.CategoryMapper;
import com.phucx.phucxfandb.repository.CategoryRepository;
import com.phucx.phucxfandb.service.category.CategoryReaderService;
import com.phucx.phucxfandb.service.image.CategoryImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryReaderServiceImp implements CategoryReaderService {
    private final CategoryRepository categoryRepository;
    private final CategoryImageService categoryImageService;
    private final CategoryMapper mapper;

    @Override
    public Page<CategoryDTO> getCategories(int pageNumber, int pageSize){
        log.info("getCategories(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Category> categories = categoryRepository.findByIsDeletedFalse(page);
        categoryImageService.setCategoriesImage(categories.getContent());
        return categories
                .map(categoryImageService::setCategoryImage)
                .map(mapper::toCategoryDTO);

    }

    @Override
    public CategoryDTO getCategory(long categoryId){
        log.info("getCategory(categoryId={})", categoryId);
        Category category = categoryRepository.findByCategoryIdAndIsDeletedFalse(categoryId)
                .orElseThrow(()-> new NotFoundException("Category", categoryId));
        return mapper.toCategoryDTO(categoryImageService.setCategoryImage(category));
    }


    @Override
    public CategoryDTO getCategory(String categoryName){
        log.info("getCategory(categoryName={})", categoryName);
        Category category = categoryRepository.findByCategoryNameAndIsDeletedFalse(categoryName)
                .orElseThrow(()-> new NotFoundException("Category", categoryName));
        return mapper.toCategoryDTO(categoryImageService.setCategoryImage(category));
    }

    @Override
    public Category getCategoryEntity(long categoryId) {
        log.info("getCategoryEntity(categoryId={})", categoryId);
        Category category = categoryRepository.findByCategoryIdAndIsDeletedFalse(categoryId)
                .orElseThrow(()-> new NotFoundException("Category", categoryId));
        return categoryImageService.setCategoryImage(category);
    }
}
