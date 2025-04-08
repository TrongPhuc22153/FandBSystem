package com.phucx.phucxfoodshop.service.search.imp;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.model.entity.CurrentProduct;
import com.phucx.phucxfoodshop.repository.CurrentProductRepository;
import com.phucx.phucxfoodshop.service.image.ProductImageService;
import com.phucx.phucxfoodshop.service.search.SearchProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchProductServiceImp implements SearchProductService{
    private final CurrentProductRepository currentProductRepository;
    private final ProductImageService productImageService;

    // search product by name like
    @Override
    public Page<CurrentProduct> searchCurrentProducts(String productName, int pageNumber, int pageSize) {
        log.info("searchCurrentProducts(productName={}, pageNumber={}, pageSize={})", productName, pageNumber, pageSize);
        String searchValue = "%"+productName+"%";
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProduct> productsPageable = currentProductRepository.searchCurrentProductsByProductName(searchValue, page);
        log.info("SearchProducts: {}", productsPageable.getContent());
        productImageService.setCurrentProductsImage(productsPageable.getContent());
        return productsPageable;
    }

    @Override
    public Page<CurrentProduct> getRecommendedProductsByCategory(
        int productID, String categoryName, int pageNumber, int pageSize) {
        
        log.info("getRecommendedProductsByCategory(productID={}, categoryName={}, pageNumber={}, pageSize={})", 
            productID, categoryName, pageNumber, pageSize);
        // replace '-' with "_" for like syntax in sql server
        categoryName = categoryName.replaceAll("-", "_");
            
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProduct> products = currentProductRepository
            .findRandomLikeCategoryNameWithoutProductID(productID, categoryName, page);
        productImageService.setCurrentProductsImage(products.getContent());
        return products;
    }
    
    @Override
    public List<CurrentProduct> getRecommendedProducts(int pageNumber, int pageSize) {
        log.info("getRecommendedProducts(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CurrentProduct> productsPageable = currentProductRepository.findProductsRandom(pageable);
        return this.productImageService.setCurrentProductsImage(productsPageable.getContent());
    }
    
}
