package com.phucx.phucxfandb.service.category.impl;

import com.phucx.phucxfandb.dto.request.RequestCategoryDTO;
import com.phucx.phucxfandb.dto.response.CategoryDTO;
import com.phucx.phucxfandb.entity.Category;
import com.phucx.phucxfandb.exception.EntityExistsException;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.CategoryMapper;
import com.phucx.phucxfandb.repository.CategoryRepository;
import com.phucx.phucxfandb.service.category.CategoryUpdateService;
import com.phucx.phucxfandb.service.image.CategoryImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryUpdateServiceImpl implements CategoryUpdateService {
    private final CategoryRepository categoryRepository;
    private final CategoryImageService categoryImageService;
    private final CategoryMapper mapper;

    @Override
    @Modifying
    @Transactional
    public CategoryDTO updateCategory(Long categoryId, RequestCategoryDTO requestCategoryDTO){
        log.info("updateCategory(id={}, requestCategoryDTO={})", categoryId, requestCategoryDTO);
        Category existingCategory = categoryRepository.findByCategoryIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new NotFoundException("Category", categoryId));
        mapper.updateCategory(requestCategoryDTO, existingCategory);
        // Save the updated category
        Category updatedCategory = categoryRepository.save(existingCategory);
        updatedCategory = categoryImageService.setCategoryImage(updatedCategory);
        return mapper.toCategoryDTO(updatedCategory);
    }

    @Override
    @Modifying
    @Transactional
    public CategoryDTO createCategory(RequestCategoryDTO createCategoryDTO) {
        log.info("createCategory(createCategoryDTO={})", createCategoryDTO);
        if(categoryRepository.existsByCategoryName(createCategoryDTO.getCategoryName())){
            throw new EntityExistsException("Category " + createCategoryDTO.getCategoryName() + " already exists");
        }
        Category category = mapper.toCategory(createCategoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return mapper.toCategoryDTO(savedCategory);
    }

    @Override
    @Modifying
    @Transactional
    public List<CategoryDTO> createCategories(List<RequestCategoryDTO> createCategoryDTOs) {
        log.info("createCategories(createCategoriesDTO={})", createCategoryDTOs);

        List<Category> categoriesToSave = createCategoryDTOs.stream()
                .map(mapper::toCategory)
                .collect(Collectors.toList());

        return categoryRepository.saveAll(categoriesToSave).stream()
                .map(mapper::toCategoryDTO)
                .collect(Collectors.toList());
    }
}
