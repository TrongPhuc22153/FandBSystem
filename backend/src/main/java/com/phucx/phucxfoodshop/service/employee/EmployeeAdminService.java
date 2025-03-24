package com.phucx.phucxfoodshop.service.employee;

import org.springframework.data.domain.Page;

import com.phucx.phucxfoodshop.constant.UserSearch;
import com.phucx.phucxfoodshop.model.EmployeeAdminDetails;
import com.phucx.phucxfoodshop.model.UserDetails;

public interface EmployeeAdminService {
    // update employee information for admin
    public EmployeeAdminDetails updateAdminEmployeeInfo(EmployeeAdminDetails employee);
    // get employee
    public EmployeeAdminDetails getEmployeeAdminDetails(String userID);
    // find users
    public Page<UserDetails> getByUsernameLike(String username, Integer pageNumber);
    public Page<UserDetails> getByFirstnameLike(String firstname, Integer pageNumber);
    public Page<UserDetails> getByLastnameLike(String lastname, Integer pageNumber);
    public Page<UserDetails> getByEmailLike(String email, Integer pageNumber);
    public Page<UserDetails> getUsers(UserSearch searchPara, String searchValue, Integer pageNumber);
}
