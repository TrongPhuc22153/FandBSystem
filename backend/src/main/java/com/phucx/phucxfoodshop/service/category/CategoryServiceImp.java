package com.phucx.phucxfoodshop.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.exceptions.EntityExistsException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.Category;
import com.phucx.phucxfoodshop.repository.CategoryRepository;
import com.phucx.phucxfoodshop.service.image.CategoryImageService;
import com.phucx.phucxfoodshop.utils.ImageUtils;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImp implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryImageService categoryImageService;

    public List<Category> getCategories(){
        return categoryImageService.setCategoriesImage(categoryRepository.findAll());
    }

    public Page<Category> getCategories(int pageNumber, int pageSize){
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Category> categories = categoryRepository.findAll(page);
        categoryImageService.setCategoriesImage(categories.getContent());
        return categories;
    }

    @Override
    public Category getCategory(int categoryID) throws NotFoundException {
        Category category = categoryRepository.findById(categoryID)
        .orElseThrow(()-> new NotFoundException("Category " + categoryID + " does not found"));
        return categoryImageService.setCategoryImage(category);
    }


    @Override
    public Category getCategory(String categoryName) throws NotFoundException {
        Category category = categoryRepository.findByCategoryName(categoryName)
            .orElseThrow(()-> new NotFoundException("Category " + categoryName + " does not found"));
        return categoryImageService.setCategoryImage(category);
    }

	@Override
	public Category updateCategory(Category category) throws NotFoundException {
        log.info("updateCategory({})", category);
        if(category.getCategoryID()==null) throw new NullPointerException("Category Id is null");
        Integer categoryID = category.getCategoryID();
        Category fetchedCategory = categoryRepository.findById(categoryID)
        .orElseThrow(()-> new NotFoundException("Category " + categoryID + " does not found"));
        // extract image's name from url
        String picture = ImageUtils.getImageName(category.getPicture());
        // update category
        Integer check = categoryRepository.updateCategory(
            category.getCategoryName(), category.getDescription(), 
            picture, fetchedCategory.getCategoryID());
        if(check<=0) throw new RuntimeException("Category "+ category.getCategoryID() + " can not be updated");
        category.setPicture(picture);
        categoryImageService.setCategoryImage(category);
        return category;
	}
    
	@Override
    @Modifying
    @Transactional
	public Boolean createCategory(Category category) throws EntityExistsException {
        log.info("createCategory({})", category);
        Optional<Category> fetchedCategoryOp = categoryRepository.findByCategoryName(category.getCategoryName());
        if(fetchedCategoryOp.isPresent()){
            throw new EntityExistsException("Category " + fetchedCategoryOp.get().getCategoryName() + " is already existed");
        }
        // extract image's name from url
        String picture = ImageUtils.getImageName(category.getPicture());
        // add new category
        Boolean result = categoryRepository.addCategory(category.getCategoryName(), category.getDescription(), picture);
        return result;
        
	}

    @Override
    public List<Category> getCategoryLike(String categoryName) {
        log.info("getCategoryLike({})", categoryName);
        return categoryRepository.findByCategoryNameLike(categoryName);
    }
}
