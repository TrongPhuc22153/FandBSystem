package com.phucx.phucxfoodshop.service.customer;

import java.util.List;

import org.springframework.data.domain.Page;

import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.InvalidUserException;
import com.phucx.phucxfoodshop.model.CustomerDetail;
import com.phucx.phucxfoodshop.model.CustomerFullDetails;
import com.phucx.phucxfoodshop.model.UserDetails;

public interface CustomerService {
    // get customer
    public CustomerDetail getCustomerByID(String customerID) throws CustomerNotFoundException;
    public CustomerDetail getCustomerByUserID(String userID) throws CustomerNotFoundException;
    public CustomerDetail getCustomerByUsername(String username) throws CustomerNotFoundException;
    public CustomerDetail getCustomerDetail(String userID) throws CustomerNotFoundException;
    
    public List<CustomerDetail> getCustomersByUserIDs(List<String> userIDs);
    public List<CustomerDetail> getCustomersByIDs(List<String> customerIDs);

    public CustomerDetail updateCustomerInfo(CustomerDetail customer) throws CustomerNotFoundException;
    public CustomerDetail addNewCustomer(CustomerDetail customer) throws InvalidUserException;

    public void sendVerificationEmail(String username, String baseUrl);

    public CustomerFullDetails getCustomerDetails(String userID);
    public CustomerFullDetails getCustomerDetailsByUsername(String username);

    public Page<UserDetails> getUsers(Integer pagenumber);
}
