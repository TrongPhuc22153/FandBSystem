package com.phucx.phucxfandb.service.image.imp;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.phucxfandb.config.FileProperties;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.entity.Employee;
import com.phucx.phucxfandb.service.image.EmployeeImageService;
import com.phucx.phucxfandb.utils.ImageUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeImageServiceImp implements EmployeeImageService{

    @Value("${spring.application.name}")
    private String serverName;
    @Autowired
    private FileProperties fileProperties;

    private final String imageUri = "/api/v1/image/employee";

    @Override
    public Employee setEmployeeDetailImage(Employee employee) {
        // filtering employee 
        if(!(employee.getProfile().getPicture()!=null && !employee.getProfile().getPicture().isEmpty())) return employee;
        // employee has image
        String imageUrl = "/" + serverName + imageUri;
        employee.getProfile().setPicture(imageUrl + "/" + employee.getProfile().getPicture());
        return employee;
    }

    @Override
    public List<Employee> setEmployeeDetailImage(List<Employee> employees) {
        employees.forEach(employee ->{
            if(employee.getProfile().getPicture()!=null && !employee.getProfile().getPicture().isEmpty()){
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                employee.getProfile().setPicture(uri + "/" + employee.getProfile().getPicture());
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
        String uri = request.getRequestURI();
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
