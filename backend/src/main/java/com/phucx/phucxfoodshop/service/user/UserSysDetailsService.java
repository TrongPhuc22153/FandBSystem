package com.phucx.phucxfoodshop.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.phucx.phucxfoodshop.exceptions.UserAuthenticationException;
import com.phucx.phucxfoodshop.model.UserRegisterInfo;

public interface UserSysDetailsService extends UserDetailsService{
    // check authentication
    public Boolean checkPassword(String hashedPassword, String rawPassword);
    public Boolean checkUserAuthentication(String password, String hashedPasswored);
    // register 
    public Boolean registerCustomer(UserRegisterInfo userRegisterInfo) throws UserAuthenticationException;
    public Boolean registerEmployee(UserRegisterInfo userRegisterInfo) throws UserAuthenticationException;

    public Boolean updateUserPassword(String userID, String password);
    
}
