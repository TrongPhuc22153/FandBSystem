package com.phucx.phucxfoodshop.service.image;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.Category;

import jakarta.servlet.http.HttpServletRequest;

public interface CategoryImageService {
    public byte[] getCategoryImage(String imageName) throws IOException;
    public String uploadCategoryImage(MultipartFile file) throws IOException, NotFoundException;
    public String getCategoryMimeType(String file) throws IOException;
    public String getCurrentUrl(HttpServletRequest request);
    // set image for category
    public Category setCategoryImage(Category category);
    public List<Category> setCategoriesImage(List<Category> categories);
}
