package com.phucx.phucxfandb.service.image.imp;

import com.phucx.phucxfandb.config.FileProperties;
import com.phucx.phucxfandb.entity.Product;
import com.phucx.phucxfandb.service.image.ProductImageService;
import com.phucx.phucxfandb.utils.ImageUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ProductImageServiceImp implements ProductImageService {
    @Autowired
    private FileProperties fileProperties;
    @Value("${spring.application.name}")
    private String serverName;

    private final String imageUri = "/api/v1/image/product";

    @Override
    public byte[] getProductImage(String imageName) throws IOException {
        log.info("getProductImage({})", imageName);
        return ImageUtils.getImage(imageName, fileProperties.getProductImageLocation());
    }

    @Override
    public String uploadProductImage(MultipartFile file) throws IOException {
        log.info("uploadProductImage({})", file);
        return ImageUtils.uploadImage(file, fileProperties.getProductImageLocation());
    }

    @Override
    public String getProductMimeType(String file) throws IOException {
        log.info("getProductMimeType({})", file);
        return ImageUtils.getMimeType(file, fileProperties.getProductImageLocation());
    }

    @Override
    public List<Product> setProductsImage(List<Product> products) {
        products.forEach(product ->{
            if(product.getPicture()!=null && !product.getPicture().isEmpty()){
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
        if(!(product.getPicture()!=null && !product.getPicture().isEmpty())) return product;
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
    public String getCurrentUrl(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String url = request.getRequestURL().toString();
        String baseurl = url.substring(0, url.length()-uri.length());
        return baseurl + "/" + serverName +  imageUri;
    }

    @Override
    public String getPictureUri(String imageName) {
        String uri = "/" + serverName + imageUri;
        return uri + "/" + imageName;
    }
    
}
