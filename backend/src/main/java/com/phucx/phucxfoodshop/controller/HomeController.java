package com.phucx.phucxfoodshop.controller;

import org.springframework.web.bind.annotation.RestController;

import com.phucx.phucxfoodshop.constant.WebConstant;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.entity.Category;
import com.phucx.phucxfoodshop.model.entity.CurrentProduct;
import com.phucx.phucxfoodshop.model.entity.ProductDetail;
import com.phucx.phucxfoodshop.service.category.CategoryService;
import com.phucx.phucxfoodshop.service.product.ProductService;
import com.phucx.phucxfoodshop.service.search.SearchProductService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/shop/home", produces = MediaType.APPLICATION_JSON_VALUE)
public class HomeController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final SearchProductService searchProductService;

    // Category
    @Operation(summary = "Get all categories", tags = {"category", "get", "public"})
    @GetMapping(value = "categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Category>> getCategories(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<Category> data = categoryService
            .getCategories(pageNumber, WebConstant.PAGE_SIZE);

        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Get category by name", tags = {"category", "get", "public"})
    @GetMapping(value = "categories/name/{categoryName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> getCategory(
        @PathVariable(name = "categoryName") String categoryName
    ) throws NotFoundException{
        Category data = categoryService.getCategory(categoryName);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Get category by id", tags = {"category", "get", "public"})
    @GetMapping(value = "categories/id/{categoryID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> getCategoryByID(
        @PathVariable(name = "categoryID") Integer categoryID
    ) throws NotFoundException{
        Category data = categoryService.getCategory(categoryID);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Get products by category", tags = {"product", "get", "public"})
    @GetMapping(value = "categories/{categoryName}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CurrentProduct>> getProductsByCategoryName(
        @PathVariable(name = "categoryName") String categoryName,
        @RequestParam(name = "page", required=false) Integer pageNumber
    ) throws NotFoundException{
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<CurrentProduct> products = productService.getCurrentProductsByCategoryName(
            categoryName, pageNumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(products);
    }


    // products
    @Operation(summary = "Get products", tags = {"product", "get", "public"})
    @GetMapping(value = "products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CurrentProduct>> getProducts(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber:0;
        var productsPageable =productService.getCurrentProduct(pageNumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(productsPageable);
    }
    
    @Operation(summary = "Get product by id", tags = {"product", "get", "public"})
    @GetMapping(value = "products/id/{productID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDetail> getProductByID(@PathVariable(name = "productID") Integer productID) throws NotFoundException{
        ProductDetail productDetails = productService.getProductDetail(productID);
        return ResponseEntity.ok().body(productDetails);
    }

    @Operation(summary = "Get recommended products", tags = {"product", "get", "public"})
    @GetMapping(value = "/products/recommended", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CurrentProduct>> getRecommendedProducts(){
        List<CurrentProduct> products = searchProductService
            .getRecommendedProducts(0, WebConstant.RECOMMENDED_PRODUCT_PAGE_SIZE);
            
        return ResponseEntity.ok().body(products);
    }
}
