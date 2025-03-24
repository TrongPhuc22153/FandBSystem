package com.phucx.phucxfoodshop.service.image.imp;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.phucxfoodshop.config.FileProperties;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.Category;
import com.phucx.phucxfoodshop.service.image.CategoryImageService;
import com.phucx.phucxfoodshop.utils.ImageUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryImageServiceImp implements CategoryImageService{
    @Value("${spring.application.name}")
    private String serverName;
    @Autowired
    private FileProperties fileProperties;

    private final String imageUri = "/image/category";

    @Override
    public Category setCategoryImage(Category category) {
        // filtering product 
        if(!(category.getPicture()!=null && category.getPicture().length()>0)) return category;
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
        categories.stream().forEach(category ->{
            if(category.getPicture()!=null && category.getPicture().length()>0){
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
        String uri = request.getRequestURI().toString();
        String url = request.getRequestURL().toString();
        String baseurl = url.substring(0, url.length()-uri.length());
        return baseurl + "/" + serverName +  imageUri;
    }
}
