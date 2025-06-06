package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.constant.ValidationGroups;
import com.phucx.phucxfandb.dto.request.ProductRequestParamsDTO;
import com.phucx.phucxfandb.dto.request.RequestProductDTO;
import com.phucx.phucxfandb.dto.response.ProductDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.product.ProductReaderService;
import com.phucx.phucxfandb.service.product.ProductUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/products", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Products", description = "Public and Admin operations for products")
public class ProductController {
    private final ProductReaderService productReaderService;
    private final ProductUpdateService productUpdateService;

    @GetMapping
    @Operation(summary = "Get products", description = "Public access")
    public ResponseEntity<Page<ProductDTO>> getProducts(@ModelAttribute ProductRequestParamsDTO params) {
        Page<ProductDTO> productDTOS = productReaderService.getProducts(params);
        return ResponseEntity.ok(productDTOS);
    }


    @GetMapping("{id}")
    @Operation(summary = "Get product by id", description = "Public access")
    public ResponseEntity<ProductDTO> getProductByID(
            @RequestParam(name = "isDeleted", required = false) Boolean isDeleted,
            @PathVariable(name = "id") Integer productID) {
        ProductDTO product = productReaderService.getProduct(productID, isDeleted);
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
                .message("Product updated successfully")
                .data(updatedProduct)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping(value = "{id}/quantity", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update product quantity", description = "Employee access")
    public ResponseEntity<ResponseDTO<ProductDTO>> updateProductQuantity(
            @Validated(ValidationGroups.UpdateProductQuantity.class)
            @RequestBody RequestProductDTO requestProductDTO,
            @PathVariable Long id
    ){
        ProductDTO updatedProduct = productUpdateService.updateProductQuantity(id, requestProductDTO.getUnitsInStock());
        var response = ResponseDTO.<ProductDTO>builder()
                .message("Product quantity updated successfully")
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
                .message("Product created successfully")
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

    @PatchMapping("{id}")
    @Operation(summary = "Update product is deleted status", description = "Admin access")
    public ResponseEntity<ResponseDTO<ProductDTO>> updateProductIsDeletedStatus(
            @PathVariable long id,
            @RequestBody RequestProductDTO requestProductDTO
    ) {
        var data = productUpdateService.updateProductIsDeletedStatus(id, requestProductDTO);
        ResponseDTO<ProductDTO> response = ResponseDTO.<ProductDTO>builder()
                .message("Product updated successfully")
                .data(data)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
