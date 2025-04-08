package com.phucx.phucxfoodshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.phucxfoodshop.constant.UserSearch;
import com.phucx.phucxfoodshop.exceptions.UserNotFoundException;
import com.phucx.phucxfoodshop.model.dto.CustomerAdminDetails;
import com.phucx.phucxfoodshop.model.dto.EmployeeAdminDetails;
import com.phucx.phucxfoodshop.model.dto.ResponseFormat;
import com.phucx.phucxfoodshop.model.dto.UserRegisterInfo;
import com.phucx.phucxfoodshop.model.entity.CustomerDetail;
import com.phucx.phucxfoodshop.model.entity.EmployeeDetail;
import com.phucx.phucxfoodshop.model.entity.UserDetails;
import com.phucx.phucxfoodshop.service.customer.CustomerAdminService;
import com.phucx.phucxfoodshop.service.customer.CustomerService;
import com.phucx.phucxfoodshop.service.employee.EmployeeAdminService;
import com.phucx.phucxfoodshop.service.employee.EmployeeService;
import com.phucx.phucxfoodshop.service.user.UserPasswordService;
import com.phucx.phucxfoodshop.service.user.UserSysDetailsService;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/account/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final EmployeeAdminService employeeAdminService;
    private final CustomerAdminService customerAdminService;
    private final UserPasswordService userPasswordService;
    private final UserSysDetailsService userSysDetailsService;

    @Operation(summary = "Check user role", 
        tags = {"get", "check", "admin"},
        description = "Check whether a user is admin or not")
    @GetMapping(value = "/isAdmin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseFormat> isAdmin(){
        ResponseFormat format = new ResponseFormat();
        format.setStatus(true);
        return ResponseEntity.ok().body(format);
    }

    @Operation(summary = "Get user by CustomerID", 
        tags = {"get", "customer info", "admin"})
    @GetMapping(value = "/customers/{customerID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDetail> getUserByCustomerID(
        @PathVariable(name = "customerID") String customerID
    ) throws UserNotFoundException{
        CustomerDetail customer = customerService.getCustomerByID(customerID);
        return ResponseEntity.ok().body(customer);
    }

    @Operation(summary = "Get employee by EmployeeID", 
        tags = {"get", "employee info", "admin"})
    @GetMapping(value = "/employees/{employeeID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeDetail> getEmployeeDetail(
        @PathVariable(name = "employeeID") String employeeID
    ) throws UserNotFoundException{
        EmployeeDetail employee = employeeService.getEmployee(employeeID);
        return ResponseEntity.ok().body(employee);
    }
    

    @Operation(summary = "Update employee information", tags = {"post", "update employee", "admin"})
    @PostMapping(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseFormat> updateEmployeeDetail(
        @RequestBody EmployeeAdminDetails employee
    ){
        EmployeeAdminDetails updatedEmployee = employeeAdminService.updateAdminEmployeeInfo(employee);
        Boolean status = updatedEmployee!=null?true:false;
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Update customer information", tags = {"post", "update customer", "admin"})
    @PostMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseFormat> updateCustomerDetails(
        @RequestBody CustomerAdminDetails customer
    ){
        CustomerAdminDetails updatedCustomer = customerAdminService.updateAdminCustomerInfo(customer);
        Boolean status = updatedCustomer!=null?true:false;
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(summary = "Register employee", tags = {"put", "admin", "employee"})
    @PostMapping(value = "/registerEmployee", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseFormat> registerEmployee(
        @RequestBody UserRegisterInfo userRegisterInfo
    ){
        Boolean status = userSysDetailsService.registerEmployee(userRegisterInfo);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }


    @GetMapping("/employees")
    @Operation(summary = "Get employees", tags = {"get", "admin", "employees"})
    public ResponseEntity<Page<UserDetails>> getEmployees(
        @RequestParam(name = "page", required = false) Integer pagenumber
    ){
        if(pagenumber==null) pagenumber = 0;
        Page<UserDetails> users = employeeService.getUsers(pagenumber);
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/customers")
    @Operation(summary = "Get customers", tags = {"get", "admin", "customers"})
    public ResponseEntity<Page<UserDetails>> getCusmoters(
        @RequestParam(name = "page", required = false) Integer pagenumber
    ){
        if(pagenumber==null) pagenumber = 0;
        Page<UserDetails> users = customerService.getUsers(pagenumber);
        return ResponseEntity.ok().body(users);
    }

    @Operation(summary = "Get customer user by userId", tags = {"get", "customer info", "admin"})
    @GetMapping(value = "/customers/user/{userID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerAdminDetails> getCustomerUserByUserID(@PathVariable(name = "userID") String userID){
        CustomerAdminDetails customer = customerAdminService.getCustomerAdminDetails(userID);
        return ResponseEntity.ok().body(customer);
    }

    @Operation(summary = "Get employee user by userId", tags = {"get", "employee info", "admin"})
    @GetMapping(value = "/employees/user/{userID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeAdminDetails> getEmployeeUserByUserID(@PathVariable(name = "userID") String userID){
        EmployeeAdminDetails employee = employeeAdminService.getEmployeeAdminDetails(userID);
        return ResponseEntity.ok().body(employee);
    }

    @Operation(summary = "Reset user password", tags = {"post", "change password", "admin"})
    @PostMapping(value = "/user/{userID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseFormat> resetUserPassword(@PathVariable(name = "userID") String userID){
        Boolean result = userPasswordService.resetUserPasswordRandom(userID);
        ResponseFormat responseFormat = new ResponseFormat(result);
        return ResponseEntity.ok().body(responseFormat);
    }

    @Operation(summary = "Search for customers", tags = {"get", "search", "admin"})
    @GetMapping(value = "/customers/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserDetails>> searchForCustomers(
        @RequestParam(name = "searchType") UserSearch searchType,
        @RequestParam(name = "searchValue") String searchValue,
        @RequestParam(name = "page", required = false) Integer page
    ){
        if(page==null) page = 0;
        Page<UserDetails> users = customerAdminService.getUsers(
            searchType, searchValue, page);
        return ResponseEntity.ok().body(users);
    }

    @Operation(summary = "Search for employees", tags = {"get", "search", "admin"})
    @GetMapping(value = "/employees/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserDetails>> searchForEmployees(
        @RequestParam(name = "searchType") UserSearch searchType,
        @RequestParam(name = "searchValue") String searchValue,
        @RequestParam(name = "page", required = false) Integer page
    ){
        if(page==null) page = 0;
        Page<UserDetails> users = employeeAdminService.getUsers(
            searchType, searchValue, page);
        return ResponseEntity.ok().body(users);
    }

}
