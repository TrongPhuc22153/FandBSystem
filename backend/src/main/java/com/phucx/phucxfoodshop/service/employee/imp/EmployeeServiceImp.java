package com.phucx.phucxfoodshop.service.employee.imp;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.exceptions.InvalidUserException;
import com.phucx.phucxfoodshop.model.EmployeeDetail;
import com.phucx.phucxfoodshop.model.EmployeeDetails;
import com.phucx.phucxfoodshop.model.EmployeeDetailsBuilder;
import com.phucx.phucxfoodshop.model.User;
import com.phucx.phucxfoodshop.model.UserDetails;
import com.phucx.phucxfoodshop.repository.EmployeeDetailRepostiory;
import com.phucx.phucxfoodshop.service.employee.EmployeeService;
import com.phucx.phucxfoodshop.service.image.EmployeeImageService;
import com.phucx.phucxfoodshop.service.user.UserService;
import com.phucx.phucxfoodshop.utils.ImageUtils;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImp implements EmployeeService{
    @Autowired
    private EmployeeDetailRepostiory employeeDetailRepostiory;
    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeImageService employeeImageService;

    private final String EMPLOYEE = "EMPLOYEE";

	@Override
	public EmployeeDetail updateEmployeeInfo(EmployeeDetail employee){
        log.info("updateEmployeeInfo({})", employee.toString());
        EmployeeDetail fetchedEmployee =employeeDetailRepostiory.findById(employee.getEmployeeID())
            .orElseThrow(()-> new EmployeeNotFoundException("Employee " + employee.getEmployeeID() + " does not found")); 
        String picture = ImageUtils.getImageName(employee.getPicture());
        // update employee 
        Boolean result = employeeDetailRepostiory.updateEmployeeInfo(
            fetchedEmployee.getEmployeeID(), employee.getBirthDate(), 
            employee.getAddress(), employee.getCity(), employee.getDistrict(),
            employee.getWard(),  employee.getPhone(), picture); 
        if(!result) throw new RuntimeException("Employee " + employee.getEmployeeID() + " can not be updated!");

        employee.setPicture(picture);
        employeeImageService.setEmployeeDetailImage(employee);
        return employee;
	}

    @Override
    public EmployeeDetail getEmployee(String employeeID) {
        EmployeeDetail employee = employeeDetailRepostiory.findById(employeeID)
            .orElseThrow(()-> new EmployeeNotFoundException("Employee " + employeeID + " does not found"));
        employeeImageService.setEmployeeDetailImage(employee);
        return employee;
    }


    @Override
    public EmployeeDetail addNewEmployee(EmployeeDetail employeedDetail) throws InvalidUserException {
        log.info("addNewEmployee({})", employeedDetail);
        if(employeedDetail.getUserID()==null)throw new InvalidUserException("UserId is missing");

        Optional<EmployeeDetail> fetchedEmployee= employeeDetailRepostiory.findByUserID(employeedDetail.getUserID());
        if(fetchedEmployee.isPresent()) throw new EntityExistsException("User " + employeedDetail.getUserID() + " already exists");
        // add new employee
        String profileID = UUID.randomUUID().toString();
        String employeeID = UUID.randomUUID().toString();

       Boolean status = employeeDetailRepostiory.addNewEmployee(profileID, employeedDetail.getUserID(), employeeID);
       if(!status) throw new RuntimeException("Error when creating new employee profile!");

       return new EmployeeDetail(employeeID, employeedDetail.getUserID());
    }

    @Override
    public EmployeeDetail getEmployeeByUserID(String userID) {
        EmployeeDetail fetchedEmployee = employeeDetailRepostiory.findByUserID(userID)
            .orElseThrow(()-> new EmployeeNotFoundException("Employee with UserID: " + userID + " does not found"));
        return employeeImageService.setEmployeeDetailImage(fetchedEmployee);
    }

    @Override
    public Page<EmployeeDetail> getEmployees(Integer pageNumber, Integer pageSize) {
        log.info("getEmployees(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeDetail> employees = employeeDetailRepostiory.findAll(pageable);
        employeeImageService.setEmployeeDetailImage(employees.getContent());
        return employees;
    }

    @Override
    public EmployeeDetail getEmployeeDetail(String userID) throws InvalidUserException{
        log.info("getEmployeeDetail(userID={})", userID);
        Optional<EmployeeDetail> fetchedEmployeeOptional = employeeDetailRepostiory.findByUserID(userID);
        if(fetchedEmployeeOptional.isPresent()){
            EmployeeDetail fetchedEmployee = fetchedEmployeeOptional.get();
            employeeImageService.setEmployeeDetailImage(fetchedEmployee);
            return fetchedEmployee;
        }else{
            EmployeeDetail newEmployeeDetail = this.addNewEmployee(new EmployeeDetail(userID));
            return newEmployeeDetail;
        }
    }
    
    @Override
    public EmployeeDetails getEmployeeDetails(String userID) throws InvalidUserException {
        log.info("getEmployeeDetails(userID={})", userID);
        EmployeeDetail employeeDetail = this.getEmployeeDetail(userID);
        User user = userService.getUserById(userID);
        EmployeeDetails employeeDetails  = new EmployeeDetailsBuilder()
            .withEmployeeID(employeeDetail.getEmployeeID())
            .withBirthDate(employeeDetail.getBirthDate())
            .withHireDate(employeeDetail.getHireDate())
            .withPhone(employeeDetail.getPhone())
            .withPicture(employeeDetail.getPicture())
            .withTitle(employeeDetail.getTitle())
            .withAddress(employeeDetail.getAddress())
            .withCity(employeeDetail.getCity())
            .withDistrict(employeeDetail.getDistrict())
            .withWard(employeeDetail.getWard())
            .withNotes(employeeDetail.getNotes())
            .withUserID(user.getUserID())
            .withUsername(user.getUsername())
            .withFirstName(user.getFirstName())
            .withLastName(user.getLastName())
            .withEmail(user.getEmail())
            .build();
        return employeeDetails;
    }

    @Override
    public List<EmployeeDetail> getEmployees(List<String> userIds) {
        log.info("getEmployees(userIds={})", userIds);
        List<EmployeeDetail> employees = employeeDetailRepostiory.findAllByUserID(userIds);
        employeeImageService.setEmployeeDetailImage(employees);
        return employees;
    }

    @Override
    public EmployeeDetail getEmployeeDetailByUsername(String username) throws InvalidUserException{
        log.info("getEmployeeDetailByUsername(username={})", username);
        String userid = userService.getUser(username).getUserID();
        return this.getEmployeeDetail(userid);
    }

    @Override
    public EmployeeDetails getEmployeeDetailsByUsername(String username) throws InvalidUserException{
        log.info("getEmployeeDetailsByUsername(username={})", username);
        String userid = userService.getUser(username).getUserID();
        return this.getEmployeeDetails(userid);
    }

    @Override
    public Page<UserDetails> getUsers(Integer pageNumber) {
        log.info("getUsers(pageNumber={})", pageNumber);
        Page<UserDetails> users = this.userService.getUsersByRole(EMPLOYEE, pageNumber);
        users.getContent().stream().forEach(employee -> {
            if(employee.getPicture()!=null){
                String imageUrl = employeeImageService.getImageUrl(employee.getPicture());
                employee.setPicture(imageUrl);
            }
        });
        return users;
    }
}
