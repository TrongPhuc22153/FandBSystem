package com.phucx.phucxfandb.service.category.impl;

import com.phucx.phucxfandb.dto.request.RequestCategoryDTO;
import com.phucx.phucxfandb.dto.response.CategoryDTO;
import com.phucx.phucxfandb.entity.Category;
import com.phucx.phucxfandb.exception.EntityExistsException;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.CategoryMapper;
import com.phucx.phucxfandb.repository.CategoryRepository;
import com.phucx.phucxfandb.repository.ProductRepository;
import com.phucx.phucxfandb.service.category.CategoryUpdateService;
import com.phucx.phucxfandb.service.image.ImageUpdateService;
import com.phucx.phucxfandb.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryUpdateServiceImpl implements CategoryUpdateService {
    private final CategoryRepository categoryRepository;
    private final ImageUpdateService imageUpdateService;
    private final ProductRepository productRepository;
    private final CategoryMapper mapper;

    @Override
    @Modifying
    @Transactional
    public CategoryDTO updateCategory(Long categoryId, RequestCategoryDTO requestCategoryDTO){
        log.info("updateCategory(id={}, requestCategoryDTO={})", categoryId, requestCategoryDTO);
        Category existingCategory = categoryRepository.findByCategoryIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new NotFoundException("Category", categoryId));
        // upload new image
        if(requestCategoryDTO.getPicture()!=null && !requestCategoryDTO.getPicture().isEmpty()){
            String newImageName = ImageUtils.extractImageNameFromUrl(requestCategoryDTO.getPicture());
            if(existingCategory.getPicture() != null ){
                if(!newImageName.equalsIgnoreCase(existingCategory.getPicture())){
                    imageUpdateService.removeImages(List.of(existingCategory.getPicture()));
                    requestCategoryDTO.setPicture(newImageName);
                }else{
                    requestCategoryDTO.setPicture(existingCategory.getPicture());
                }
            }else{
                requestCategoryDTO.setPicture(newImageName);
            }
        }
        // update category
        mapper.updateCategory(requestCategoryDTO, existingCategory);
        // Save the updated category
        Category updatedCategory = categoryRepository.save(existingCategory);
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

        // upload new image
        if(createCategoryDTO.getPicture()!=null && !createCategoryDTO.getPicture().isEmpty()){
            String imageName = ImageUtils.extractImageNameFromUrl(createCategoryDTO.getPicture());
            createCategoryDTO.setPicture(imageName);
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

    @Override
    @Modifying
    @Transactional
    public CategoryDTO updateCategoryIsDeleted(long id, RequestCategoryDTO requestCategoryDTO) {
        log.info("updateCategoryIsDeleted(id={}, requestCategory={})", id, requestCategoryDTO);
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Category", id));
        if(!existingCategory.getIsDeleted()){
            if(productRepository.existsByCategoryCategoryIdAndIsDeletedFalse(id)){
                throw new IllegalArgumentException(String.format("Cannot delete category with id %d because it has associated products", id));
            }
        }
        existingCategory.setIsDeleted(requestCategoryDTO.getIsDeleted());
        Category updated = categoryRepository.save(existingCategory);
        return mapper.toCategoryDTO(updated);
    }
}
