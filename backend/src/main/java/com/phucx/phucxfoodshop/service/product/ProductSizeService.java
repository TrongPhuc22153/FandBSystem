package com.phucx.phucxfoodshop.service.product;

import java.util.List;

import com.phucx.phucxfoodshop.exceptions.EntityExistsException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.ProductSize;
import com.phucx.phucxfoodshop.model.ProductSizeInfo;

public interface ProductSizeService {
    public Boolean updateProductSize(ProductSize productSize) throws NotFoundException;
    // insert product
    public Boolean createProduct(ProductSizeInfo productSizeInfo) throws EntityExistsException;

    public Boolean createProductSize(ProductSize productSize) throws NotFoundException;
    public Boolean createProductSizes(List<ProductSize> productSizes) throws NotFoundException;
    public ProductSize getProductSize(Integer productID) throws NotFoundException;
    public void updateProductSizeByProductName(List<ProductSizeInfo> productSizeInfos) throws NotFoundException;
    
}
