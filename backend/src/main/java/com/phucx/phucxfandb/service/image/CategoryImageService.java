package com.phucx.phucxfandb.service.image;

import com.phucx.phucxfandb.entity.Category;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryImageService {
    byte[] getCategoryImage(String imageName) throws IOException;
    String uploadCategoryImage(MultipartFile file) throws IOException;
    String getCategoryMimeType(String file) throws IOException;
    String getCurrentUrl(HttpServletRequest request);
    // set image for category
    Category setCategoryImage(Category category);
    List<Category> setCategoriesImage(List<Category> categories);
}
