package com.phucx.phucxfandb.service.category.impl;

import com.phucx.phucxfandb.dto.response.CategoryDTO;
import com.phucx.phucxfandb.entity.Category;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.CategoryMapper;
import com.phucx.phucxfandb.repository.CategoryRepository;
import com.phucx.phucxfandb.service.category.CategoryReaderService;
import com.phucx.phucxfandb.service.image.ImageReaderService;
import com.phucx.phucxfandb.specifications.CategorySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryReaderServiceImp implements CategoryReaderService {
    private final CategoryRepository categoryRepository;
    private final ImageReaderService imageReaderService;
    private final CategoryMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO> getCategories(int pageNumber, int pageSize, String sortBy, Sort.Direction direction, Boolean isDeleted){
        Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        Specification<Category> spec = Specification
                .where(CategorySpecification.hasIsDeleted(isDeleted));
        return categoryRepository.findAll(spec, page)
                .map(this::setImageUrl)
                .map(mapper::toCategoryDTO);

    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategory(long id, Boolean isDeleted) {
        log.info("getCategory(id={}, isDeleted={})", id, isDeleted);
        Optional<Category> category;
        if(isDeleted==null){
            category = categoryRepository.findById(id);
        }else{
            category = categoryRepository.findByCategoryIdAndIsDeleted(id, isDeleted);
        }
        return category.map(this::setImageUrl)
                .map(mapper::toCategoryDTO)
                .orElseThrow(()-> new NotFoundException(Category.class.getSimpleName(), id));
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryEntity(long categoryId) {
        log.info("getCategoryEntity(categoryId={})", categoryId);
        return categoryRepository.findByCategoryIdAndIsDeletedFalse(categoryId)
                .orElseThrow(()-> new NotFoundException(Category.class.getSimpleName(), categoryId));
    }

    private Category setImageUrl(Category category){
        if(!(category.getPicture()==null || category.getPicture().isEmpty())){
            String imageUrl = imageReaderService.getImageUrl(category.getPicture());
            category.setPicture(imageUrl);
        }
        return category;
    }
}
