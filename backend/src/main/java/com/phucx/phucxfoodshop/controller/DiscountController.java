package com.phucx.phucxfoodshop.controller;

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

import com.phucx.phucxfoodshop.constant.WebConstant;
import com.phucx.phucxfoodshop.exceptions.InvalidDiscountException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.Discount;
import com.phucx.phucxfoodshop.model.DiscountDetail;
import com.phucx.phucxfoodshop.model.DiscountType;
import com.phucx.phucxfoodshop.model.DiscountWithProduct;
import com.phucx.phucxfoodshop.model.ResponseFormat;
import com.phucx.phucxfoodshop.service.discount.DiscountService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/shop/discount", produces = MediaType.APPLICATION_JSON_VALUE)
public class DiscountController {
    private final DiscountService discountService;
     // discount

    @Operation(summary = "Add new discount", tags = {"discount", "put", "admin"})
    @PutMapping
    public ResponseEntity<ResponseFormat> insertDiscount(
        @RequestBody DiscountWithProduct discount
    ) throws InvalidDiscountException, RuntimeException, NotFoundException{
        Discount newDiscount = discountService.insertDiscount(discount);
        boolean status = newDiscount!=null?true:false;
        ResponseFormat data = new ResponseFormat(status);

        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Update discount", tags = {"discount", "post", "admin"})
    @PostMapping
    public ResponseEntity<ResponseFormat> updateDiscount(
        @RequestBody DiscountWithProduct discount
    ) throws InvalidDiscountException, NotFoundException{
        Boolean status = discountService.updateDiscount(discount);
        ResponseFormat data = new ResponseFormat(status);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Update discount status", tags = {"discount", "post", "admin"})
    @PostMapping("/status")
    public ResponseEntity<ResponseFormat> updateDiscountStatus(
        @RequestBody DiscountDetail discount
    ) throws InvalidDiscountException, NotFoundException{
        Boolean check = discountService.updateDiscountStatus(discount);
        ResponseFormat data = new ResponseFormat(check);
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Get discounts of a product", tags = {"discount", "get", "admin"})
    @GetMapping("/product/{productID}")
    public ResponseEntity<Page<DiscountDetail>> getDiscountsByProductID(
        @PathVariable(name = "productID") Integer productID,
        @RequestParam(name = "page", required = false) Integer pageNumber
    ) throws NotFoundException{
        pageNumber = pageNumber!=null?pageNumber: 0;
        Page<DiscountDetail> discounts = discountService.getDiscountsByProduct(
            productID, pageNumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(discounts);
    }

    @Operation(summary = "Get discount by id", tags = {"discount", "get", "admin"})
    @GetMapping("/{discountID}")
    public ResponseEntity<DiscountDetail> getDiscountDetail(
        @PathVariable(name = "discountID") String discountID
    ) throws NotFoundException{
        DiscountDetail discount = discountService.getDiscountDetail(discountID);
        return ResponseEntity.ok().body(discount);
    }

    @Operation(summary = "Get discount's types ", tags = {"discount", "get", "admin"})
    @GetMapping("/type")
    public ResponseEntity<Page<DiscountType>> getDiscountTypes(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber: 0;
        Page<DiscountType> types = discountService.getDiscountTypes(
            pageNumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(types);
    }
}
