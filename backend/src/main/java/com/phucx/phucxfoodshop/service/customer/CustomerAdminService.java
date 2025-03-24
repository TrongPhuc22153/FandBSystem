package com.phucx.phucxfoodshop.service.customer;

import org.springframework.data.domain.Page;

import com.phucx.phucxfoodshop.constant.UserSearch;
import com.phucx.phucxfoodshop.model.CustomerAdminDetails;
import com.phucx.phucxfoodshop.model.UserDetails;

public interface CustomerAdminService {
    // update employee information for admin
    public CustomerAdminDetails updateAdminCustomerInfo(CustomerAdminDetails customer);
    // get employee
    public CustomerAdminDetails getCustomerAdminDetails(String userID);
    // find users
    public Page<UserDetails> getByUsernameLike(String username, Integer pageNumber);
    public Page<UserDetails> getByFirstnameLike(String firstname, Integer pageNumber);
    public Page<UserDetails> getByLastnameLike(String lastname, Integer pageNumber);
    public Page<UserDetails> getByEmailLike(String email, Integer pageNumber);
    public Page<UserDetails> getUsers(UserSearch searchParam, String searchValue, Integer pageNumber);
}
