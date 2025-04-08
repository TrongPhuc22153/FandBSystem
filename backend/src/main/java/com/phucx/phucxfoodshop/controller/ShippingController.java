package com.phucx.phucxfoodshop.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.constant.CookieConstant;
import com.phucx.phucxfoodshop.exceptions.EmptyCartException;
import com.phucx.phucxfoodshop.exceptions.InvalidOrderException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.dto.District;
import com.phucx.phucxfoodshop.model.dto.Province;
import com.phucx.phucxfoodshop.model.dto.ShippingResponse;
import com.phucx.phucxfoodshop.model.dto.Ward;
import com.phucx.phucxfoodshop.service.shipper.ShippingService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/shipping", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShippingController {
    private final ShippingService shippingService;

    @GetMapping("/cost")
    @Operation(summary = "Estimate shipping cost", tags = {"customer", "shipping"})
    public ResponseEntity<ShippingResponse> shippingCost(
        @RequestParam(name = "wardCode") String wardCode,
        @RequestParam(name = "districtId") Integer districtID,
        @RequestParam(name = "cityId") Integer cityId,
        @CookieValue(name = CookieConstant.CART_COOKIE) String cartJson,
        Authentication authentication
    ) throws NotFoundException, JsonProcessingException, EmptyCartException, InvalidOrderException {
        String username = authentication.getName();
        ShippingResponse response = shippingService.costEstimate(
            cityId, districtID, wardCode, username, cartJson);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/provinces")
    @Operation(summary = "Get provinces", tags = {"customer", "shipping"})
    public ResponseEntity<List<Province>> getProvinces() {
        List<Province> result = shippingService.getProvinces();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/districts")
    @Operation(summary = "Get district", tags = {"customer", "shipping"})
    public ResponseEntity<List<District>> getDistrict(@RequestParam(name = "provinceId") Integer provinceId) {
        List<District> result = shippingService.getDistricts(provinceId);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/wards")
    @Operation(summary = "Get wards", tags = {"customer", "shipping"})
    public ResponseEntity<List<Ward>> getWards(@RequestParam(name = "districtId") Integer districtId) {
        List<Ward> result = shippingService.getWards(districtId);
        return ResponseEntity.ok().body(result);
    }
    
}
