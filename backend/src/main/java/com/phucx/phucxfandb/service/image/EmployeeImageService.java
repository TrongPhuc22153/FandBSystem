package com.phucx.phucxfandb.service.image;

import com.phucx.phucxfandb.entity.Employee;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EmployeeImageService {
    byte[] getEmployeeImage(String imageName) throws IOException;
    String uploadEmployeeImage(MultipartFile file) throws IOException;
    String getCurrentUrl(HttpServletRequest request);
    String getMimeType(String imageName) throws IOException;

    // set employee image
    String getImageUrl(String picture);
    Employee setEmployeeDetailImage(Employee employee);
    List<Employee> setEmployeeDetailImage(List<Employee> employees);
}
