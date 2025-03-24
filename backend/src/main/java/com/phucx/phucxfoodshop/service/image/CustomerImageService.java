package com.phucx.phucxfoodshop.service.image;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.CustomerDetail;

import jakarta.servlet.http.HttpServletRequest;

public interface CustomerImageService {
    
    public byte[] getCustomerImage(String imageName) throws IOException;
    public String uploadCustomerImage(MultipartFile file) throws IOException, NotFoundException;
    public String getCurrentUrl(HttpServletRequest request);
    public String getMimeType(String imageName) throws IOException;

    // set customer image
    public String getImageUrl(String picture);
    public CustomerDetail setCustomerDetailImage(CustomerDetail customer);
    public List<CustomerDetail> setCustomerDetailImage(List<CustomerDetail> customers);
}
