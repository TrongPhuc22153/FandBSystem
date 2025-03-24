package com.phucx.phucxfoodshop.service.employee;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.phucxfoodshop.model.EmployeeDetail;
import com.phucx.phucxfoodshop.model.EmployeeDetails;
import com.phucx.phucxfoodshop.model.UserDetails;
import com.phucx.phucxfoodshop.exceptions.InvalidUserException;

public interface EmployeeService {
    // get employee
    public EmployeeDetail getEmployee(String employeeID);
    public EmployeeDetail getEmployeeByUserID(String userID);
    public Page<EmployeeDetail> getEmployees(Integer pageNumber, Integer pageSize);
    public List<EmployeeDetail> getEmployees(List<String> userIds);
    public EmployeeDetail getEmployeeDetail(String userID) throws InvalidUserException;
    public EmployeeDetail getEmployeeDetailByUsername(String username) throws InvalidUserException;
    public EmployeeDetails getEmployeeDetails(String userID) throws InvalidUserException;
    public EmployeeDetails getEmployeeDetailsByUsername(String username) throws InvalidUserException;
    // update / create employee information
    public EmployeeDetail addNewEmployee(EmployeeDetail employeeDetail) throws InvalidUserException;
    public EmployeeDetail updateEmployeeInfo(EmployeeDetail employee);  
    // get empmloyee users
    public Page<UserDetails> getUsers(Integer pageNumber);
}
