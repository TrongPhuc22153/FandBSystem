package com.phucx.phucxfandb.service.image;

import com.phucx.phucxfandb.entity.Product;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductImageService {
    // get image
    public byte[] getProductImage(String imageName) throws IOException;
    // upload image
    public String uploadProductImage(MultipartFile file) throws IOException;
    // get mimetype for response
    public String getProductMimeType(String file) throws IOException;

    public String getCurrentUrl(HttpServletRequest request);
    public String getPictureUri(String imageName);
    // set image for product
    public List<Product> setProductsImage(List<Product> products);
    public Product setProductImage(Product product);
}
