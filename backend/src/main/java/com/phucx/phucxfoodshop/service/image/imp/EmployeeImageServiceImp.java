package com.phucx.phucxfoodshop.service.image.imp;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.phucxfoodshop.config.FileProperties;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.EmployeeDetail;
import com.phucx.phucxfoodshop.service.image.EmployeeImageService;
import com.phucx.phucxfoodshop.utils.ImageUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeImageServiceImp implements EmployeeImageService{

    @Value("${spring.application.name}")
    private String serverName;
    @Autowired
    private FileProperties fileProperties;

    private final String imageUri = "/image/employee";

    @Override
    public EmployeeDetail setEmployeeDetailImage(EmployeeDetail employee) {
        // filtering employee 
        if(!(employee.getPicture()!=null && employee.getPicture().length()>0)) return employee;
        // employee has image
        String imageUrl = "/" + serverName + imageUri;
        employee.setPicture(imageUrl + "/" + employee.getPicture());
        return employee;
    }

    @Override
    public List<EmployeeDetail> setEmployeeDetailImage(List<EmployeeDetail> employees) {
        employees.stream().forEach(employee ->{
            if(employee.getPicture()!=null && employee.getPicture().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                employee.setPicture(uri + "/" + employee.getPicture());
            }
        });
        return employees;
    }

    @Override
    public byte[] getEmployeeImage(String imageName) throws IOException {
        log.info("getEmployeeImage({})", imageName);
        return ImageUtils.getImage(imageName, fileProperties.getEmployeeImageLocation());
    }

    @Override
    public String uploadEmployeeImage(MultipartFile file) throws IOException, NotFoundException {
        log.info("uploadEmployeeImage({})", file);
        return ImageUtils.uploadImage(file, fileProperties.getEmployeeImageLocation());
    }

    @Override
    public String getCurrentUrl(HttpServletRequest request) {
        log.info("getCurrentUrl()");
        String uri = request.getRequestURI().toString();
        String url = request.getRequestURL().toString();
        String baseurl = url.substring(0, url.length()-uri.length());
        return baseurl + "/" + serverName +  imageUri;
    }

    @Override
    public String getMimeType(String imageName) throws IOException {
        log.info("getMimeType({})", imageName);
        return ImageUtils.getMimeType(imageName, fileProperties.getEmployeeImageLocation());
    }

    @Override
    public String getImageUrl(String picture) {
        String uri = "/" + serverName + imageUri;
        return uri + "/" + picture;
    }
}
