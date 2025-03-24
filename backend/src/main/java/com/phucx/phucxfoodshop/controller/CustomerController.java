package com.phucx.phucxfoodshop.controller;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.CreditCard;
import com.phucx.phucxfoodshop.model.CustomerDetail;
import com.phucx.phucxfoodshop.model.CustomerFullDetails;
import com.phucx.phucxfoodshop.model.ImageFormat;
import com.phucx.phucxfoodshop.model.ResponseFormat;
import com.phucx.phucxfoodshop.service.creditcard.CreditCardService;
import com.phucx.phucxfoodshop.service.customer.CustomerService;
import com.phucx.phucxfoodshop.service.image.CustomerImageService;
import com.phucx.phucxfoodshop.utils.ServerUrlUtils;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/account/customer", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerImageService customerImageService;
    private final CreditCardService creditCardService;

    @Operation(summary = "Check user's role", 
        tags = {"get", "check", "customer"})
    @GetMapping("/isCustomer")
    public ResponseEntity<ResponseFormat> isCustomer(){
        return ResponseEntity.ok().body(new ResponseFormat(true));
    }
    // GET CUSTOMER'S INFOMATION
    @Operation(summary = "Get customer information", 
        tags = {"get", "customer info", "customer"})
    @GetMapping("/info")
    public ResponseEntity<CustomerFullDetails> getUserInfo(Authentication authentication){
        String username = authentication.getName();
        CustomerFullDetails customer = customerService.getCustomerDetailsByUsername(username);
        return ResponseEntity.ok().body(customer);
    }
    // UPDATE CUSTOMER'S INFOMATION
    @Operation(summary = "Update customer information", 
        tags = {"post", "update customer", "customer"})
    @PostMapping("/info")
    public ResponseEntity<ResponseFormat> updateUserInfo(
        @RequestBody CustomerDetail customer
    ) throws CustomerNotFoundException{
        CustomerDetail updatedCustomer = customerService.updateCustomerInfo(customer);
        return ResponseEntity.ok().body(new ResponseFormat(updatedCustomer!=null?true: false));
    }

    // get credit card
    @Operation(summary = "Get customer's credit card", 
        tags = {"get", "credit card", "customer"})
    @GetMapping(value = "/credit")
    public ResponseEntity<CreditCard> getCreditCard(
        Authentication authentication
    ) throws NotFoundException {
        CreditCard creditCard = this.creditCardService.getCreditCardByUsername(authentication.getName());
        return ResponseEntity.ok().body(creditCard);
    }

    // update credit card
    @Operation(summary = "Update customer's credit card", 
        tags = {"post", "update credit card", "customer"})
    @PostMapping(value = "/credit")
    public ResponseEntity<ResponseFormat> updateCreditCard(
        Authentication authentication,
        @RequestBody CreditCard creditCard
    ) throws NotFoundException {
        Boolean status = this.creditCardService.updateCreditCardByUsername(creditCard, authentication.getName());
        ResponseFormat responseFormat = new ResponseFormat(status);
        return ResponseEntity.ok().body(responseFormat);
    }

    // set image
    @Operation(summary = "Upload customer's image", 
        tags = {"post", "upload image", "customer", "image"})
    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageFormat> uploadCustomerImage(
        @RequestBody MultipartFile file, HttpServletRequest request
    ) throws IOException, NotFoundException {
        String filename = customerImageService.uploadCustomerImage(file);
        String imageUrl = customerImageService.getCurrentUrl(request) + "/" + filename;
        ImageFormat imageFormat = new ImageFormat(imageUrl);
        return ResponseEntity.ok().body(imageFormat);
    }

    @PostMapping("/sendEmailVerification")
    @Operation(summary = "Send verification email to customer", tags = {"customer", "post"})
    public ResponseEntity<ResponseFormat> sendEmailVerification (
        HttpServletRequest request, Authentication authentication
    ){
        String baseUrl = ServerUrlUtils.getBaseUrl(request);
        customerService.sendVerificationEmail(authentication.getName(), baseUrl);
        ResponseFormat responseFormat = new ResponseFormat(true);
        return ResponseEntity.ok().body(responseFormat);
    }
}
