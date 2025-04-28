package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestCustomerDTO;
import com.phucx.phucxfandb.dto.response.CustomerDTO;
import com.phucx.phucxfandb.dto.response.ImageDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.customer.CustomerReaderService;
import com.phucx.phucxfandb.service.customer.CustomerUpdateService;
import com.phucx.phucxfandb.service.image.CustomerImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Customer endpoint")
@RequestMapping(value = "/api/v1/customers/profile", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerProfileController {
    private final CustomerReaderService customerReaderService;
    private final CustomerUpdateService customerUpdateService;
    private final CustomerImageService customerImageService;

    @GetMapping("/me")
    @Operation(summary = "Get signed in user endpoint", description = "Customer access")
    public ResponseEntity<CustomerDTO> getCustomer(Principal principal){
        CustomerDTO customer = customerReaderService
                .getCustomerByUsername(principal.getName());
        return ResponseEntity.ok().body(customer);
    }

    @PutMapping(value = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update customer information", description = "Customer access")
    public ResponseEntity<ResponseDTO<CustomerDTO>> updateUserInfo(
            Principal principal,
            @Valid @RequestBody RequestCustomerDTO requestCustomerDTO
    ){
        CustomerDTO customerDTO = customerUpdateService.updateCustomerByUsername(principal.getName(), requestCustomerDTO);
        ResponseDTO<CustomerDTO> responseDTO = ResponseDTO.<CustomerDTO>builder()
                .message("Your profile updated successfully")
                .data(customerDTO)
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    // set image
    @Operation(summary = "Upload customer's image", description = "Customer access")
    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<ImageDTO>> uploadCustomerImage(
        @RequestBody MultipartFile file, HttpServletRequest request
    ) throws IOException{
        String filename = customerImageService.uploadCustomerImage(file);
        String imageUrl = customerImageService.getCurrentUrl(request) + "/" + filename;
        ImageDTO imageFormat = ImageDTO.builder()
                .imageUrl(imageUrl)
                .build();
        ResponseDTO<ImageDTO> responseDTO = ResponseDTO.<ImageDTO>builder()
                .message("Your image uploaded successfully")
                .data(imageFormat)
                .build();
        return ResponseEntity.ok(responseDTO);
    }

//    @PostMapping("/sendEmailVerification")
//    @Operation(summary = "Send verification email to customer", tags = {"customer", "post"})
//    public ResponseEntity<ResponseFormat> sendEmailVerification (
//        HttpServletRequest request, Authentication authentication
//    ){
//        String baseUrl = ServerUrlUtils.getBaseUrl(request);
//        customerService.sendVerificationEmail(authentication.getName(), baseUrl);
//        ResponseFormat responseFormat = ResponseFormat.builder()
//                .status(true)
//                .build();
//        return ResponseEntity.ok().body(responseFormat);
//    }
}
