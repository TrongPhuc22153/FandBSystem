package com.phucx.phucxfoodshop.service.product;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.phucxfoodshop.exceptions.EntityExistsException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.dto.ProductDetails;
import com.phucx.phucxfoodshop.model.dto.ProductDiscountsDTO;
import com.phucx.phucxfoodshop.model.dto.ProductStockTableType;
import com.phucx.phucxfoodshop.model.dto.ResponseFormat;
import com.phucx.phucxfoodshop.model.dto.SellingProduct;
import com.phucx.phucxfoodshop.model.entity.CurrentProduct;
import com.phucx.phucxfoodshop.model.entity.ExistedProduct;
import com.phucx.phucxfoodshop.model.entity.Product;
import com.phucx.phucxfoodshop.model.entity.ProductDetail;

public interface ProductService {
    // update product's instocks
    public Boolean updateProductsInStocks(List<ProductStockTableType> productStocks) throws NotFoundException;

    // validate products
    // validaate products and update product instock
    public ResponseFormat validateAndProcessProducts(List<ProductDiscountsDTO> products);
    // validate products and product's discounts
    public ResponseFormat validateProducts(List<ProductDiscountsDTO> products);
    // update product quantity
    public Boolean updateProductInStock(List<ProductStockTableType> products) throws NotFoundException;
    // update product detail
    public ProductDetail updateProductDetail(ProductDetail productDetail) throws NotFoundException;
    // insert product
    public Boolean insertProductDetail(ProductDetail productDetail) throws EntityExistsException;
    // get product
    // current product
    public CurrentProduct getCurrentProduct(int productID) throws NotFoundException;
    public List<CurrentProduct> getCurrentProduct();
    public Page<CurrentProduct> getCurrentProduct(int pageNumber, int pageSize);
    public Page<CurrentProduct> getCurrentProductsByCategoryName(String categoryName, int pageNumber, int pageSize) throws NotFoundException;

    public List<CurrentProduct> getCurrentProducts(List<Integer> productIDs);
        
    // public List<CurrentProduct> getRecommendedProducts(int pageNumber, int pageSize);
    // public Page<CurrentProduct> getRecommendedProductsByCategory(int productID, String categoryName, int pageNumber, int pageSize);
    // search product
//    public Page<CurrentProduct> searchCurrentProducts(String productName, int pageNumber, int pageSize);
    // productdetail
    public ProductDetail getProductDetail(int productID) throws NotFoundException;
    public ProductDetails getProductDetails(int productID) throws NotFoundException;
    // product
    public Product getProduct(int productID) throws NotFoundException;
    public List<Product> getProducts();

    public List<Product> getProducts(List<Integer> productIDs);
    public Page<Product> getProducts(int pageNumber, int pageSize);
    public List<Product> getProducts(String productName);
    public Page<Product> getProductsByName(int pageNumber, int pageSize, String productName);
    public Page<Product> getProductsByCategoryName(int pageNumber, int pageSize, String categoryName);
    // 
    public Page<ExistedProduct> getExistedProducts(int pageNumber, int pageSize);

    public List<SellingProduct> getTopSellingProducts(Integer year, Integer limit);

}
