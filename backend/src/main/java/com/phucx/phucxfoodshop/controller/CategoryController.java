package com.phucx.phucxfoodshop.controller;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.phucxfoodshop.exceptions.EntityExistsException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.dto.ImageFormat;
import com.phucx.phucxfoodshop.model.dto.ResponseFormat;
import com.phucx.phucxfoodshop.model.entity.Category;
import com.phucx.phucxfoodshop.service.category.CategoryService;
import com.phucx.phucxfoodshop.service.image.CategoryImageService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/shop/category", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryImageService categoryImageService;

    // category
    @Operation(summary = "Update category", tags = {"category", "post", "admin"})
    @PostMapping
    public ResponseEntity<ResponseFormat> updateCategory(
        @RequestBody Category category
    ) throws NotFoundException{
        Category updatedCategory = categoryService.updateCategory(category);
        ResponseFormat data = new ResponseFormat(updatedCategory!=null?true:false);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Add new category", tags = {"category", "put", "admin"})
    @PutMapping
    public ResponseEntity<ResponseFormat> createCategory(
        @RequestBody Category category
    ) throws EntityExistsException{
        boolean check = categoryService.createCategory(category);
        ResponseFormat data = new ResponseFormat(check);
        return ResponseEntity.ok().body(data);
    }

     // set image
     @Operation(summary = "Upload category image", tags = {"category", "post", "admin", "upload image"})
    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageFormat> uploadCategoryImage(
        @RequestBody MultipartFile file,
        HttpServletRequest request
    ) throws IOException, NotFoundException {

        String filename = categoryImageService.uploadCategoryImage(file);
        String imageUrl = categoryImageService.getCurrentUrl(request) + "/" + filename;
        ImageFormat imageFormat = new ImageFormat(imageUrl);
        return ResponseEntity.ok().body(imageFormat);
    }
}
