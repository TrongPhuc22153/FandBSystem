package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.CategoryRequestParamsDTO;
import com.phucx.phucxfandb.dto.request.RequestCategoryDTO;
import com.phucx.phucxfandb.dto.response.CategoryDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.category.CategoryReaderService;
import com.phucx.phucxfandb.service.category.CategoryUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Categories", description = "Public and Admin operations for categories")
public class CategoryController {
    private final CategoryReaderService categoryReaderService;
    private final CategoryUpdateService categoryUpdateService;

    @GetMapping
    @Operation(summary = "Get all categories", description = "Public access")
    public ResponseEntity<Page<CategoryDTO>> getCategories(@ModelAttribute CategoryRequestParamsDTO params){
        Page<CategoryDTO> data = categoryReaderService.getCategories(params);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID or name", description = "Public access")
    public ResponseEntity<CategoryDTO> getCategory(
            @Parameter(description = "Category ID to retrieve a single category")
            @PathVariable(name = "id") Long categoryId,
            @RequestParam(name = "isDeleted", required = false) Boolean isDeleted
    ){
        CategoryDTO data = categoryReaderService.getCategory(categoryId, isDeleted);
        return ResponseEntity.ok().body(data);
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update category", description = "Admin access")
    public ResponseEntity<ResponseDTO<CategoryDTO>> updateCategory(
        @Valid @RequestBody RequestCategoryDTO requestCategoryDTO,
        @PathVariable(name = "id") Long categoryId
    ){
        CategoryDTO updatedCategory = categoryUpdateService.updateCategory(categoryId, requestCategoryDTO);
        ResponseDTO<CategoryDTO> responseDTO = ResponseDTO.<CategoryDTO>builder()
                .message("Category updated successfully")
                .data(updatedCategory)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new category", description = "Admin access")
    public ResponseEntity<ResponseDTO<CategoryDTO>> createCategory(
        @Valid @RequestBody RequestCategoryDTO requestCategoryDTO
    ){
        CategoryDTO newCategory = categoryUpdateService
                .createCategory(requestCategoryDTO);
        ResponseDTO<CategoryDTO> responseDTO = ResponseDTO.<CategoryDTO>builder()
                .message("Category created successfully")
                .data(newCategory)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("{id}")
    @Operation(summary = "Delete or enable category", description = "Admin access")
    public ResponseEntity<ResponseDTO<CategoryDTO>> updateCategoryIsDeletedStatus(
            @PathVariable long id,
            @RequestBody RequestCategoryDTO requestCategoryDTO
    ) {
        var data = categoryUpdateService.updateCategoryIsDeleted(id, requestCategoryDTO);
        ResponseDTO<CategoryDTO> response = ResponseDTO.<CategoryDTO>builder()
                .message("Category updated successfully")
                .data(data)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new categories", description = "Admin access")
    public ResponseEntity<ResponseDTO<List<CategoryDTO>>> createCategories(
            @Valid @RequestBody List<RequestCategoryDTO> requestCategoryDTOs
    ){
        List<CategoryDTO> newCategories = categoryUpdateService
                .createCategories(requestCategoryDTOs);
        ResponseDTO<List<CategoryDTO>> responseDTO = ResponseDTO.<List<CategoryDTO>>builder()
                .message("Categories created successfully")
                .data(newCategories)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
