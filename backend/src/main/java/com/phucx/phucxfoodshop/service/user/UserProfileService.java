package com.phucx.phucxfoodshop.service.user;

import org.springframework.security.core.Authentication;

import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.model.UserAuthentication;
import com.phucx.phucxfoodshop.model.UserProfile;
import com.phucx.phucxfoodshop.model.UserVerification;

public interface UserProfileService {
    public UserProfile getUserProfile(String userID);
    public UserProfile getUserProfileByUsername(String username);
    public UserProfile getUserProfileByID(String profileID);

    public UserAuthentication getUserAuthentication(Authentication authentication);

    public Boolean updateProfileVerification(String profileID, Boolean status);
    public Boolean updatePhoneVerification(String profileID, Boolean status);

    public UserVerification getUserVerification(String userID);

    public UserProfile getUserProfileByCustomerID(String customerID) throws CustomerNotFoundException;
    public UserProfile getUserProfileByEmployeeID(String employeeID) throws EmployeeNotFoundException;
}
