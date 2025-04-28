package com.phucx.phucxfandb.service.image.imp;

import com.phucx.phucxfandb.config.FileProperties;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.entity.Category;
import com.phucx.phucxfandb.service.image.CategoryImageService;
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
public class CategoryImageServiceImp implements CategoryImageService{
    @Value("${spring.application.name}")
    private String serverName;
    @Autowired
    private FileProperties fileProperties;

    private final String imageUri = "/api/v1/image/category";

    @Override
    public Category setCategoryImage(Category category) {
        // filtering product 
        if(!(category.getPicture()!=null && !category.getPicture().isEmpty())) return category;
        String picture = category.getPicture();
        // setting image with image uri
        String uri = "/" + serverName + imageUri;
        if(!picture.contains(uri)){
            category.setPicture(uri + "/" + category.getPicture());
        }
        return category;
    }

    @Override
    public List<Category> setCategoriesImage(List<Category> categories) {
        categories.forEach(category ->{
            if(category.getPicture()!=null && !category.getPicture().isEmpty()){
                String picture = category.getPicture();
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                if(!picture.contains(uri)){
                    category.setPicture(uri + "/" + category.getPicture());
                }
            }
        });
        return categories;
    }

    @Override
    public byte[] getCategoryImage(String imageName) throws IOException {
        log.info("getCategoryImage({})", imageName);
        return ImageUtils.getImage(imageName, fileProperties.getCategoryImageLocation());
    }

    @Override
    public String uploadCategoryImage(MultipartFile file) throws IOException, NotFoundException {
        log.info("uploadCategoryImage({})", file);
        return ImageUtils.uploadImage(file, fileProperties.getCategoryImageLocation());
    }

    @Override
    public String getCategoryMimeType(String file) throws IOException {
        log.info("getCategoryMimeType({})", file);
        return ImageUtils.getMimeType(file, fileProperties.getCategoryImageLocation());
    }

    @Override
    public String getCurrentUrl(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String url = request.getRequestURL().toString();
        String baseurl = url.substring(0, url.length()-uri.length());
        return baseurl + "/" + serverName +  imageUri;
    }
}
