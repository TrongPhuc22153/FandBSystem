package com.phucx.phucxfoodshop.service.image.imp;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.phucxfoodshop.config.FileProperties;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.CurrentProduct;
import com.phucx.phucxfoodshop.model.ExistedProduct;
import com.phucx.phucxfoodshop.model.Product;
import com.phucx.phucxfoodshop.model.ProductDetail;
import com.phucx.phucxfoodshop.service.image.ProductImageService;
import com.phucx.phucxfoodshop.utils.ImageUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductImageServiceImp implements ProductImageService{
    @Autowired
    private FileProperties fileProperties;
    @Value("${spring.application.name}")
    private String serverName;

    private final String imageUri = "/image/product";

    @Override
    public byte[] getProductImage(String imageName) throws IOException {
        log.info("getProductImage({})", imageName);
        byte[] image = ImageUtils.getImage(imageName, fileProperties.getProductImageLocation());
        return image;
    }

    @Override
    public String uploadProductImage(MultipartFile file) throws IOException, NotFoundException {
        log.info("uploadProductImage({})", file);
        String imageName = ImageUtils.uploadImage(file, fileProperties.getProductImageLocation());
        return imageName;
    }

    @Override
    public String getProductMimeType(String file) throws IOException {
        log.info("getProductMimeType({})", file);
        String mimetype = ImageUtils.getMimeType(file, fileProperties.getProductImageLocation());
        return mimetype;
    }

    @Override
    public List<Product> setProductsImage(List<Product> products) {
        products.stream().forEach(product ->{
            if(product.getPicture()!=null && product.getPicture().length()>0){
                String picture = product.getPicture();
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                if(!picture.contains(uri)){
                    product.setPicture(uri + "/" + product.getPicture());
                }
            }
        });
        return products;
    }

    @Override
    public Product setProductImage(Product product) {
        // filtering product 
        if(!(product.getPicture()!=null && product.getPicture().length()>0)) return product;
        // product has image
        String picture = product.getPicture();
        // setting image with image uri
        String uri = "/" + serverName + imageUri;
        if(!picture.contains(uri)){
            product.setPicture(uri + "/" + product.getPicture());
        }
        return product;
    }

    @Override
    public CurrentProduct setCurrentProductImage(CurrentProduct product) {
        // filtering product 
        if(!(product.getPicture()!=null && product.getPicture().length()>0)) return product;
        String picture = product.getPicture();
        // setting image with image uri
        String uri = "/" + serverName + imageUri;
        if(!picture.contains(uri)){
            product.setPicture(uri + "/" + product.getPicture());
        }
        return product;
    }

    @Override
    public List<CurrentProduct> setCurrentProductsImage(List<CurrentProduct> products) {
        products.stream().forEach(product ->{
            if(product.getPicture()!=null && product.getPicture().length()>0){
                String picture = product.getPicture();
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                if(!picture.contains(uri)){
                    product.setPicture(uri + "/" + product.getPicture());
                }
            }
        });
        return products;
    }

    @Override
    public ProductDetail setProductDetailImage(ProductDetail product) {
        // filtering product 
        if(!(product.getPicture()!=null && product.getPicture().length()>0)) return product;
        String picture = product.getPicture();
        // setting image with image uri
        String uri = "/" + serverName + imageUri;
        if(!picture.contains(uri)){
            product.setPicture(uri + "/" + product.getPicture());
        }
        return product;
    }

    @Override
    public List<ProductDetail> setProductDetailsImage(List<ProductDetail> products) {
        products.stream().forEach(product ->{
            if(product.getPicture()!=null && product.getPicture().length()>0){
                String picture = product.getPicture();
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                if(!picture.contains(uri)){
                    product.setPicture(uri + "/" + product.getPicture());
                }
            }
        });
        return products;
    }

    @Override
    public String getCurrentUrl(HttpServletRequest request) {
        String uri = request.getRequestURI().toString();
        String url = request.getRequestURL().toString();
        String baseurl = url.substring(0, url.length()-uri.length());
        return baseurl + "/" + serverName +  imageUri;
    }

    @Override
    public ExistedProduct setExistedProductImage(ExistedProduct product) {
        // filtering product 
        if(!(product.getPicture()!=null && product.getPicture().length()>0)) return product;
        String picture = product.getPicture();
        // setting image with image uri
        String uri = "/" + serverName + imageUri;
        if(!picture.contains(uri)){
            product.setPicture(uri + "/" + product.getPicture());
        }
        return product;
    }

    @Override
    public List<ExistedProduct> setExistedProductsImage(List<ExistedProduct> products) {
        products.stream().forEach(product ->{
            if(product.getPicture()!=null && product.getPicture().length()>0){
                String picture = product.getPicture();
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                if(!picture.contains(uri)){
                    product.setPicture(uri + "/" + product.getPicture());
                }
            }
        });
        return products;
    }

    @Override
    public String getPictureUri(String imageName) {
        String uri = "/" + serverName + imageUri;
        return uri + "/" + imageName;
    }
    
}
