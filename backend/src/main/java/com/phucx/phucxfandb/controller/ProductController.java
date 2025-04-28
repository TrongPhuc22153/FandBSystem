package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestProductDTO;
import com.phucx.phucxfandb.dto.response.ImageDTO;
import com.phucx.phucxfandb.dto.response.ProductDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.image.ProductImageService;
import com.phucx.phucxfandb.service.product.ProductReaderService;
import com.phucx.phucxfandb.service.product.ProductUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/products", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Products", description = "Public and Admin operations for products")
public class ProductController {
    private final ProductReaderService productReaderService;
    private final ProductUpdateService productUpdateService;
    private final ProductImageService productImageService;

    @GetMapping
    @Operation(summary = "Get products", description = "Public access")
    public ResponseEntity<Page<ProductDTO>> getProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String searchValue,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false, defaultValue = "false") Boolean isFeatured,
            @RequestParam(defaultValue = "productId") String field,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Page<ProductDTO> products;
        if( isFeatured ){
            products = productReaderService.getFeaturedProducts(field, direction, page, size);
        }else if (productName != null) {
            products = productReaderService.getProductsByName(productName, field, direction, page, size);
        } else if (searchValue != null) {
            products = productReaderService.getProductsBySearch(searchValue, field, direction, page, size);
        } else if (categoryId != null) {
            products = productReaderService.getProductsByCategoryId(categoryId, field, direction, page, size);
        } else if (categoryName != null) {
            products = productReaderService.getProductsByCategory(categoryName, field, direction, page, size);
        } else {
            products = productReaderService.getProducts(field, direction, page, size);
        }
        return ResponseEntity.ok(products);
    }


    @GetMapping("{id}")
    @Operation(summary = "Get product by id", description = "Public access")
    public ResponseEntity<ProductDTO> getProductByID(@PathVariable(name = "id") Integer productID) {
        ProductDTO product = productReaderService.getProduct(productID);
        return ResponseEntity.ok().body(product);
    }


    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update product", description = "Admin access")
    public ResponseEntity<ResponseDTO<ProductDTO>> updateProduct(
        @Valid @RequestBody RequestProductDTO requestProductDTO,
        @PathVariable Long id
    ){
        ProductDTO updatedProduct = productUpdateService.updateProduct(id, requestProductDTO);
        var response = ResponseDTO.<ProductDTO>builder()
                .data(updatedProduct)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new product", description = "Admin access")
    public ResponseEntity<ResponseDTO<ProductDTO>> createProduct(
            @Valid @RequestBody RequestProductDTO requestProductDTO
    ){
        ProductDTO updatedProduct = productUpdateService.createProduct(requestProductDTO);
        var response = ResponseDTO.<ProductDTO>builder()
                .data(updatedProduct)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new products", description = "Admin access")
    public ResponseEntity<ResponseDTO<List<ProductDTO>>> createProducts(
            @Valid @RequestBody List<RequestProductDTO> requestProductDTOs
    ){
        List<ProductDTO> newProducts = productUpdateService.createProducts(requestProductDTOs);
        var response = ResponseDTO.<List<ProductDTO>>builder()
                .data(newProducts)
                .build();
        return ResponseEntity.ok().body(response);
    }

    // set image
    @Operation(summary = "Upload product image",  description = "Admin access")
    @PostMapping("/images/upload")
    public ResponseEntity<ResponseDTO<ImageDTO>> uploadProductImage(
            @RequestBody MultipartFile file,
            HttpServletRequest request
    ) throws IOException {

        String filename = productImageService.uploadProductImage(file);
        String imageUrl = productImageService.getCurrentUrl(request) + "/" + filename;
        ImageDTO imageDTO = ImageDTO.builder()
                .imageUrl(imageUrl)
                .build();
        ResponseDTO<ImageDTO> responseDTO = ResponseDTO.<ImageDTO>builder()
                .data(imageDTO)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
