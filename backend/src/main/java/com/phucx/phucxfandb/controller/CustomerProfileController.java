package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.RequestCustomerDTO;
import com.phucx.phucxfandb.dto.response.CustomerDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.customer.CustomerReaderService;
import com.phucx.phucxfandb.service.customer.CustomerUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Customer Profile API", description = "Customer endpoint")
@RequestMapping(value = "/api/v1/customers/profile", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerProfileController {
    private final CustomerReaderService customerReaderService;
    private final CustomerUpdateService customerUpdateService;

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
}
