package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestCategoryDTO;
import com.phucx.phucxfandb.dto.response.CategoryDTO;
import com.phucx.phucxfandb.dto.response.ImageDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.category.CategoryReaderService;
import com.phucx.phucxfandb.service.category.CategoryUpdateService;
import com.phucx.phucxfandb.service.image.CategoryImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/categories",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Categories", description = "Public and Admin operations for categories")
public class CategoryController {
    private final CategoryReaderService categoryReaderService;
    private final CategoryUpdateService categoryUpdateService;
    private final CategoryImageService categoryImageService;

    @Operation(summary = "Get all categories", description = "Public access")
    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> getCategories(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ){
        Page<CategoryDTO> data = categoryReaderService.getCategories(pageNumber, pageSize);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Get category by ID or name", description = "Public access")
    @GetMapping(value = "category")
    public ResponseEntity<CategoryDTO> getCategory(
            @Parameter(description = "Category ID to retrieve a single category", required = false)
            @RequestParam(name = "id", required = false) Integer id,
            @Parameter(description = "Category name to retrieve a single category", required = false)
            @RequestParam(name = "name", required = false) String name
    ){
        if (id != null && name != null && !name.trim().isEmpty()) {
            throw new IllegalArgumentException("Cannot provide both id and name");
        }
        if (id != null) {
            CategoryDTO data = categoryReaderService.getCategory(id);
            return ResponseEntity.ok().body(data);
        }
        if (name != null && !name.trim().isEmpty()) {
            CategoryDTO data = categoryReaderService.getCategory(name);
            return ResponseEntity.ok().body(data);
        }
        throw new IllegalArgumentException("Either id or name must be provided");
    }

    @PatchMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update category", description = "Admin access")
    public ResponseEntity<ResponseDTO<CategoryDTO>> updateCategory(
        @Valid @RequestBody RequestCategoryDTO requestCategoryDTO,
        @PathVariable Long categoryId
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

     // set image
    @Operation(summary = "Upload category image",  description = "Admin access")
    @PostMapping("/images/upload")
    public ResponseEntity<ResponseDTO<ImageDTO>> uploadCategoryImage(
        @RequestBody MultipartFile file,
        HttpServletRequest request
    ) throws IOException {

        String filename = categoryImageService.uploadCategoryImage(file);
        String imageUrl = categoryImageService.getCurrentUrl(request) + "/" + filename;
        ImageDTO imageDTO = ImageDTO.builder()
                .imageUrl(imageUrl)
                .build();
        ResponseDTO<ImageDTO> responseDTO = ResponseDTO.<ImageDTO>builder()
                .data(imageDTO)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
