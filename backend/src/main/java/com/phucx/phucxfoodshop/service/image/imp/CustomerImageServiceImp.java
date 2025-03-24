package com.phucx.phucxfoodshop.service.image.imp;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.phucxfoodshop.config.FileProperties;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.CustomerDetail;
import com.phucx.phucxfoodshop.service.image.CustomerImageService;
import com.phucx.phucxfoodshop.utils.ImageUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerImageServiceImp implements CustomerImageService{
    @Value("${spring.application.name}")
    private String serverName;
    @Autowired
    private FileProperties fileProperties;

    private final String imageUri = "/image/customer";

    @Override
    public CustomerDetail setCustomerDetailImage(CustomerDetail customer) {
        // filtering customer 
        if(!(customer.getPicture()!=null && customer.getPicture().length()>0)) return customer;
        // customer has image
        String imageUrl = "/" + serverName + imageUri;
        customer.setPicture(imageUrl + "/" + customer.getPicture());
        return customer;
    }

    @Override
    public List<CustomerDetail> setCustomerDetailImage(List<CustomerDetail> customers) {
        customers.stream().forEach(customer ->{
            if(customer.getPicture()!=null && customer.getPicture().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                customer.setPicture(uri + "/" + customer.getPicture());
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
    public String uploadCustomerImage(MultipartFile file) throws IOException, NotFoundException {
        log.info("uploadCustomerImage({})", file);
        return ImageUtils.uploadImage(file, fileProperties.getCustomerImageLocation());
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
        return ImageUtils.getMimeType(imageName, fileProperties.getCustomerImageLocation());
    }

    @Override
    public String getImageUrl(String picture) {
        String uri = "/" + serverName + imageUri;
        return uri + "/" + picture;
    }
    
}
