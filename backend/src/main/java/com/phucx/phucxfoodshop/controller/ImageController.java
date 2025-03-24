package com.phucx.phucxfoodshop.controller;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.phucx.phucxfoodshop.service.image.CategoryImageService;
import com.phucx.phucxfoodshop.service.image.CustomerImageService;
import com.phucx.phucxfoodshop.service.image.EmployeeImageService;
import com.phucx.phucxfoodshop.service.image.ProductImageService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/phucxfoodshop/image")
public class ImageController {
    private final ProductImageService productImageService;
    private final CategoryImageService categoryImageService;
    private final EmployeeImageService employeeImageService;
    private final CustomerImageService customerImageService;

    @Operation(summary = "Get employee image", 
        tags = {"get", "image", "public"})
    @GetMapping("/employee/{imageName}")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable String imageName) throws IOException {
        byte[] image = employeeImageService.getEmployeeImage(imageName);
        String mimeType = employeeImageService.getMimeType(imageName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }

    @Operation(summary = "Get customer image", 
        tags = {"get", "image", "public"})
    @GetMapping("/customer/{imageName}")
    public ResponseEntity<byte[]> getCustomerImage(@PathVariable String imageName) throws IOException {
        byte[] image = customerImageService.getCustomerImage(imageName);
        String mimeType = customerImageService.getMimeType(imageName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }

    @Operation(summary = "Get product's image", tags = {"image", "get", "public"})
    @GetMapping("/product/{imageName}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable String imageName) throws IOException {
        byte[] image = productImageService.getProductImage(imageName);
        String mimeType = productImageService.getProductMimeType(imageName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }

    @Operation(summary = "Get category's image", tags = {"image", "get", "public"})
    @GetMapping("/category/{imageName}")
    public ResponseEntity<byte[]> getCategoryImage(@PathVariable String imageName) throws IOException {
        byte[] image = categoryImageService.getCategoryImage(imageName);
        String mimeType = categoryImageService.getCategoryMimeType(imageName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }
    
}
