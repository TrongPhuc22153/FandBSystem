package com.phucx.phucxfoodshop.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.phucxfoodshop.model.dto.PaymentPerMonth;
import com.phucx.phucxfoodshop.model.dto.PaymentPercentage;
import com.phucx.phucxfoodshop.model.dto.SellingProduct;
import com.phucx.phucxfoodshop.service.payment.PaymentService;
import com.phucx.phucxfoodshop.service.product.ProductService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/payment/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentAdminController {
    private final PaymentService paymentService;
    private final ProductService productService;

    @Operation(summary = "Get revenue per month by year", tags = {"get", "payment", "admin"})
    @GetMapping(value = "/revenue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PaymentPerMonth>> getRevenue(@RequestParam(name = "year") Integer year){
        List<PaymentPerMonth> revenue = paymentService.getRevenuePerMonth(year);
        return ResponseEntity.ok().body(revenue);
    }

    @Operation(summary = "Get top selling products by year", tags = {"get", "payment", "admin"})
    @GetMapping(value = "/sellingProduct", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SellingProduct>> getTopSellingProduct(@RequestParam(name = "year") Integer year){
        List<SellingProduct> products = productService.getTopSellingProducts(year, 10);
        return ResponseEntity.ok().body(products);
    }

    @Operation(summary = "Get payment status percentage by year", tags = {"get", "payment", "admin"})
    @GetMapping(value = "/percentage", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PaymentPercentage>> getPaymentPercentageByYear(@RequestParam(name = "year") Integer year){
        List<PaymentPercentage> products = paymentService.getPaymentPercentageByYear(year);
        return ResponseEntity.ok().body(products);
    }

    @Operation(summary = "Get payment years", tags = {"get", "payment", "admin"})
    @GetMapping(value = "/years", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer>> getPaymentYears(){
        List<Integer> years = paymentService.getPaymentYears();
        return ResponseEntity.ok().body(years);
    }
}
