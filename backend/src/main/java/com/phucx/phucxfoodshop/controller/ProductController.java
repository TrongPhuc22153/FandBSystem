package com.phucx.phucxfoodshop.controller;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.phucxfoodshop.constant.WebConstant;
import com.phucx.phucxfoodshop.exceptions.EntityExistsException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.ExistedProduct;
import com.phucx.phucxfoodshop.model.ImageFormat;
import com.phucx.phucxfoodshop.model.ProductDetail;
import com.phucx.phucxfoodshop.model.ProductDetails;
import com.phucx.phucxfoodshop.model.ProductSize;
import com.phucx.phucxfoodshop.model.ProductSizeInfo;
import com.phucx.phucxfoodshop.model.ResponseFormat;
import com.phucx.phucxfoodshop.service.image.ProductImageService;
import com.phucx.phucxfoodshop.service.product.ProductService;
import com.phucx.phucxfoodshop.service.product.ProductSizeService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/shop/product", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
    private final ProductService productService;
    private final ProductSizeService productSizeService;
    private final ProductImageService productImageService;

    @Operation(summary = "Update product", tags = {"product", "post", "admin"})
    @PostMapping
    public ResponseEntity<ResponseFormat> updateProductDetail(
        @RequestBody ProductDetail productDetail
    ) throws NotFoundException{        
        ProductDetail updatedProduct = productService.updateProductDetail(productDetail);
        ResponseFormat data = new ResponseFormat(updatedProduct!=null?true:false);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Update product size", tags = {"product", "post", "admin"})
    @PostMapping("/size")
    public ResponseEntity<ResponseFormat> updateProductSize(
        @RequestBody ProductSize productSize
    ) throws NotFoundException{        
        Boolean status = productSizeService.updateProductSize(productSize);
        ResponseFormat data = new ResponseFormat(status);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Update product size infos", tags = {"product", "post", "admin"})
    @PostMapping("/sizes")
    public ResponseEntity<ResponseFormat> getProductSize(
        @RequestBody List<ProductSizeInfo> products
    ) throws NotFoundException{        
        productSizeService.updateProductSizeByProductName(products);
        ResponseFormat data = new ResponseFormat(true);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Add new product", tags = {"product", "put", "admin"})
    @PutMapping
    public ResponseEntity<ResponseFormat> insertProductDetail(
        @RequestBody ProductSizeInfo productDetail
    ) throws EntityExistsException{        
        Boolean status = productSizeService.createProduct(productDetail);
        ResponseFormat data = new ResponseFormat(status);

        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Get all products", tags = {"product", "get", "admin"},
        description = "Get all existed products in inventory")
    @GetMapping
    public ResponseEntity<Page<ExistedProduct>> getProducts(
        @RequestParam(name = "page", required = false) Integer page
    ){
        page = page!=null?page:0;
        Page<ExistedProduct> products = productService.getExistedProducts(page, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(products);
    }

    @Operation(summary = "Get product by id", tags = {"product", "get", "admin"})
    @GetMapping("/{productID}")
    public ResponseEntity<ProductDetails> getProductDetail(
        @PathVariable Integer productID
    ) throws NotFoundException{        
        ProductDetails product = productService.getProductDetails(productID);
        return ResponseEntity.ok().body(product);
    } 

    // set image
    @Operation(summary = "Upload product image", tags = {"product", "post", "admin", "upload image"})
    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageFormat> uploadProductImage(
        @RequestBody MultipartFile file,
        HttpServletRequest request
    ) throws IOException, NotFoundException {

        String filename = productImageService.uploadProductImage(file);
        String imageUrl = productImageService.getCurrentUrl(request) + "/" + filename;
        ImageFormat imageFormat = new ImageFormat(imageUrl);
        return ResponseEntity.ok().body(imageFormat);
    }



}
