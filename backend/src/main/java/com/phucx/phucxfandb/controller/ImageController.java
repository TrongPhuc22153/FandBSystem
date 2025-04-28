package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.service.image.CategoryImageService;
import com.phucx.phucxfandb.service.image.CustomerImageService;
import com.phucx.phucxfandb.service.image.EmployeeImageService;
import com.phucx.phucxfandb.service.image.ProductImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/images")
@Tag(name = "Images", description = "Public and Admin operations for products")
public class ImageController {
    private final ProductImageService productImageService;
    private final CategoryImageService categoryImageService;
    private final EmployeeImageService employeeImageService;
    private final CustomerImageService customerImageService;

    @Operation(summary = "Get employee image", description = "Public access")
    @GetMapping("/employee/{imageName}")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable String imageName) throws IOException {
        byte[] image = employeeImageService.getEmployeeImage(imageName);
        String mimeType = employeeImageService.getMimeType(imageName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }

    @Operation(summary = "Get customer image", description = "Public access")
    @GetMapping("/customer/{imageName}")
    public ResponseEntity<byte[]> getCustomerImage(@PathVariable String imageName) throws IOException {
        byte[] image = customerImageService.getCustomerImage(imageName);
        String mimeType = customerImageService.getMimeType(imageName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }

    @Operation(summary = "Get product's image", description = "Public access")
    @GetMapping("/product/{imageName}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable String imageName) throws IOException {
        byte[] image = productImageService.getProductImage(imageName);
        String mimeType = productImageService.getProductMimeType(imageName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }

    @Operation(summary = "Get category's image", description = "Public access")
    @GetMapping("/category/{imageName}")
    public ResponseEntity<byte[]> getCategoryImage(@PathVariable String imageName) throws IOException {
        byte[] image = categoryImageService.getCategoryImage(imageName);
        String mimeType = categoryImageService.getCategoryMimeType(imageName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }
    
}
