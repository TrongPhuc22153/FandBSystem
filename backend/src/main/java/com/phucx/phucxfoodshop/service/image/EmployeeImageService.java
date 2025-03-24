package com.phucx.phucxfoodshop.service.image;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.EmployeeDetail;

import jakarta.servlet.http.HttpServletRequest;

public interface EmployeeImageService {
    public byte[] getEmployeeImage(String imageName) throws IOException;
    public String uploadEmployeeImage(MultipartFile file) throws IOException, NotFoundException;
    public String getCurrentUrl(HttpServletRequest request);
    public String getMimeType(String imageName) throws IOException;

    // set employee image
    public String getImageUrl(String picture);
    public EmployeeDetail setEmployeeDetailImage(EmployeeDetail employee);
    public List<EmployeeDetail> setEmployeeDetailImage(List<EmployeeDetail> employees);
}
