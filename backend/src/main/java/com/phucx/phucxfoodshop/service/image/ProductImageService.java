package com.phucx.phucxfoodshop.service.image;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.CurrentProduct;
import com.phucx.phucxfoodshop.model.ExistedProduct;
import com.phucx.phucxfoodshop.model.Product;
import com.phucx.phucxfoodshop.model.ProductDetail;

import jakarta.servlet.http.HttpServletRequest;

public interface ProductImageService {
    // get image
    public byte[] getProductImage(String imageName) throws IOException;
    // upload image
    public String uploadProductImage(MultipartFile file) throws IOException, NotFoundException;
    // get mimetype for response
    public String getProductMimeType(String file) throws IOException;

    public String getCurrentUrl(HttpServletRequest request);
    public String getPictureUri(String imageName);
    // set image for product
    public List<Product> setProductsImage(List<Product> products);
    public Product setProductImage(Product product);
    // set image for current product
    public CurrentProduct setCurrentProductImage(CurrentProduct product);
    public List<CurrentProduct> setCurrentProductsImage(List<CurrentProduct> products);
    // set image for existed product
    public ExistedProduct setExistedProductImage(ExistedProduct product);
    public List<ExistedProduct> setExistedProductsImage(List<ExistedProduct> products);
    // set image for productDetail
    public ProductDetail setProductDetailImage(ProductDetail product);
    public List<ProductDetail> setProductDetailsImage(List<ProductDetail> products);
}
