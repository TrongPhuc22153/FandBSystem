package com.phucx.phucxfandb.service.image.imp;

import com.phucx.phucxfandb.config.FileProperties;
import com.phucx.phucxfandb.entity.Customer;
import com.phucx.phucxfandb.service.image.CustomerImageService;
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
public class CustomerImageServiceImp implements CustomerImageService {
    @Value("${spring.application.name}")
    private String serverName;
    @Autowired
    private FileProperties fileProperties;

    private final String imageUri = "/api/v1/image/customer";

    @Override
    public Customer setCustomerImage(Customer customer) {
        // filtering customer 
        if(!(customer.getProfile().getPicture()!=null && !customer.getProfile().getPicture().isEmpty())) return customer;
        // customer has image
        String imageUrl = "/" + serverName + imageUri;
        customer.getProfile().setPicture(imageUrl + "/" + customer.getProfile().getPicture());
        return customer;
    }

    @Override
    public List<Customer> setCustomerImage(List<Customer> customers) {
        customers.forEach(customer ->{
            if(customer.getProfile().getPicture()!=null && !customer.getProfile().getPicture().isEmpty()){
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                customer.getProfile().setPicture(uri + "/" + customer.getProfile().getPicture());
            }
        });
        return customers;
    }

    @Override
    public byte[] getCustomerImage(String imageName) throws IOException {
        log.info("getCustomerImage({})", imageName);
        return ImageUtils.getImage(imageName, fileProperties.getCustomerImageLocation());
    }

    @Override
    public String uploadCustomerImage(MultipartFile file) throws IOException{
        log.info("uploadCustomerImage({})", file);
        return ImageUtils.uploadImage(file, fileProperties.getCustomerImageLocation());
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
        return ImageUtils.getMimeType(imageName, fileProperties.getCustomerImageLocation());
    }

    @Override
    public String getImageUrl(String picture) {
        String uri = "/" + serverName + imageUri;
        return uri + "/" + picture;
    }
    
}
