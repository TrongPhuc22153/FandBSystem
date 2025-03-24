package com.phucx.phucxfoodshop.controller;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.exceptions.InvalidUserException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.RoleNotFoundException;
import com.phucx.phucxfoodshop.exceptions.ShipperNotFoundException;
import com.phucx.phucxfoodshop.exceptions.UserNotFoundException;
import com.phucx.phucxfoodshop.model.EmployeeDetail;
import com.phucx.phucxfoodshop.model.EmployeeDetails;
import com.phucx.phucxfoodshop.model.ImageFormat;
import com.phucx.phucxfoodshop.model.ResponseFormat;
import com.phucx.phucxfoodshop.service.employee.EmployeeService;
import com.phucx.phucxfoodshop.service.image.EmployeeImageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/account/employee", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeImageService employeeImageService;


    @Operation(summary = "Check user role", 
        tags = {"get", "check", "employee"})
    @GetMapping("/isEmployee")
    public ResponseEntity<ResponseFormat> isEmployee(){
        return ResponseEntity.ok().body(new ResponseFormat(true));
    }
    // GET EMPLOYEE'S INFORMATION
    @Operation(summary = "Get employee information", 
        tags = {"get", "employee info", "employee"})
    @GetMapping("/info")
    public ResponseEntity<EmployeeDetails> getUserInfo(Authentication authentication) 
    throws InvalidUserException {
        EmployeeDetails employee = employeeService.getEmployeeDetailsByUsername(authentication.getName());
        return ResponseEntity.ok().body(employee);
    }
    // UPDATE EMPLOYEE'S INFORMATION
    @Operation(summary = "Update user info", 
        tags = {"post", "update employee", "employee"})
    @PostMapping("/info")
    public ResponseEntity<ResponseFormat> updateUserInfo(
        @RequestBody EmployeeDetail employee
    ) throws EmployeeNotFoundException, JsonProcessingException{
        EmployeeDetail updatedEmployeeDetail = employeeService.updateEmployeeInfo(employee);
        return ResponseEntity.ok().body(new ResponseFormat(updatedEmployeeDetail!=null?true:false));
    }

    // set image
    @Operation(summary = "Upload employee image", tags = {"post", "upload image", "image", "employee"})
    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageFormat> uploadEmployeeImage(
        @RequestBody MultipartFile file, HttpServletRequest request
    ) throws IOException, NotFoundException {

        String filename = employeeImageService.uploadEmployeeImage(file);
        String imageUrl = employeeImageService.getCurrentUrl(request) + "/" + filename;
        ImageFormat imageFormat = new ImageFormat(imageUrl);
        return ResponseEntity.ok().body(imageFormat);
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ResponseFormat> handleIOException(IOException exception){
        log.error("Error: {}", exception.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = InvalidUserException.class)
    protected ResponseEntity<ResponseFormat> handleInvalidUserException(InvalidUserException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    protected ResponseEntity<ResponseFormat> handleUserNotFoundException(UserNotFoundException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = CustomerNotFoundException.class)
    protected ResponseEntity<ResponseFormat> handleCustomerNotFoundException(CustomerNotFoundException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = EmployeeNotFoundException.class)
    protected ResponseEntity<ResponseFormat> handleEmployeeNotFoundException(EmployeeNotFoundException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = ShipperNotFoundException.class)
    protected ResponseEntity<ResponseFormat> handleShipperNotFoundException(ShipperNotFoundException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = RoleNotFoundException.class)
    protected ResponseEntity<ResponseFormat> handleRoleNotFoundException(RoleNotFoundException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<ResponseFormat> handleIllegalArgumentException(IllegalArgumentException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = NullPointerException.class)
    protected ResponseEntity<ResponseFormat> handleNullPointerException(NullPointerException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<ResponseFormat> handleNotFoundException(NotFoundException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = EntityExistsException.class)
    protected ResponseEntity<ResponseFormat> handleEntityExistsException(EntityExistsException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<ResponseFormat> handleRuntimeException(RuntimeException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
