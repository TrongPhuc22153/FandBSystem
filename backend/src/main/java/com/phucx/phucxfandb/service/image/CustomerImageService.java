package com.phucx.phucxfandb.service.image;

import com.phucx.phucxfandb.entity.Customer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CustomerImageService {
    
    public byte[] getCustomerImage(String imageName) throws IOException;
    public String uploadCustomerImage(MultipartFile file) throws IOException;
    public String getCurrentUrl(HttpServletRequest request);
    public String getMimeType(String imageName) throws IOException;

    // set customer image
    public String getImageUrl(String picture);
    public Customer setCustomerImage(Customer customer);
    public List<Customer> setCustomerImage(List<Customer> customers);
}
