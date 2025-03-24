package com.phucx.phucxfoodshop.service.search;

import com.phucx.phucxfoodshop.model.CurrentProduct;

import java.util.List;

import org.springframework.data.domain.Page;

public interface SearchProductService {
    Page<CurrentProduct> searchCurrentProducts(String productName, int pageNumber, int pageSize);
    List<CurrentProduct> getRecommendedProducts(int pageNumber, int pageSize);
    Page<CurrentProduct> getRecommendedProductsByCategory(int productID, String categoryName, int pageNumber, int pageSize);
}
