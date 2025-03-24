package com.phucx.phucxfoodshop.service.user.imp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.constant.WebConstant;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.exceptions.UserNotFoundException;
import com.phucx.phucxfoodshop.model.User;
import com.phucx.phucxfoodshop.model.UserAuthentication;
import com.phucx.phucxfoodshop.model.UserInfo;
import com.phucx.phucxfoodshop.model.UserProfile;
import com.phucx.phucxfoodshop.model.UserVerification;
import com.phucx.phucxfoodshop.repository.UserProfileRepository;
import com.phucx.phucxfoodshop.repository.UserVerificationRepository;
import com.phucx.phucxfoodshop.service.user.UserProfileService;
import com.phucx.phucxfoodshop.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserProfileServiceImp implements UserProfileService {
    @Autowired
    private  UserProfileRepository userProfileRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserVerificationRepository userVerificationRepository;

    @Override
    public UserProfile getUserProfile(String userID) throws UserNotFoundException {
        log.info("getUserProfile(userID={})", userID);
        UserProfile user = userProfileRepository.findByUserID(userID)
            .orElseThrow(()-> new UserNotFoundException("User profile with userId " + userID + " does not found"));
        return user;
    }
    @Override
    public UserProfile getUserProfileByID(String userProfileID) throws UserNotFoundException {
        log.info("getUserProfileByID(userProfileID={})", userProfileID);
        UserProfile user = userProfileRepository.findById(userProfileID)
            .orElseThrow(()-> new UserNotFoundException("User profile " + userProfileID + " does not found"));
        return user;
    }
    
    @Override
    public UserProfile getUserProfileByCustomerID(String customerID) throws CustomerNotFoundException {
        log.info("getUserProfileByCustomerID({})", customerID);
        return userProfileRepository.findByCustomerID(customerID)
            .orElseThrow(()-> new CustomerNotFoundException("Customer "+customerID+" does not found"));
    }
    @Override
    public UserProfile getUserProfileByEmployeeID(String employeeID) throws EmployeeNotFoundException {
        log.info("getUserProfileByEmployeeID({})", employeeID);
        return userProfileRepository.findByEmployeeID(employeeID)
            .orElseThrow(()-> new EmployeeNotFoundException("Employee "+ employeeID + " does not found"));
    }
    @Override
    public UserAuthentication getUserAuthentication(Authentication authentication){
        log.info("getUserAuthentication(username={})", authentication.getName());
        User fetchedUser = userService.getUser(authentication.getName());
        List<String> roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .map(role -> role.substring(WebConstant.ROLE_PREFIX.length()))
            .collect(Collectors.toList());

        UserInfo userInfo = new UserInfo(fetchedUser.getUserID(), fetchedUser.getUsername(), fetchedUser.getEmail());
        UserAuthentication userAuthentication = new UserAuthentication(userInfo, roles);
        return userAuthentication;
    }
    @Override
    public Boolean updateProfileVerification(String profileID, Boolean status) {
        log.info("updateProfileVerification(profileID={}, status={})", profileID, status);
        Boolean result = userVerificationRepository.updateProfileVerification(profileID, status);
        return result;
    }
    @Override
    public Boolean updatePhoneVerification(String profileID, Boolean status) {
        log.info("updatePhoneVerification(profileID={}, status={})", profileID, status);
        Boolean result = userVerificationRepository.updatePhoneVerification(profileID, status);
        return result;
    }
    @Override
    public UserVerification getUserVerification(String userID) throws UserNotFoundException {
        log.info("getUserVerification(userID={})", userID);
        UserVerification userVerification = userVerificationRepository.findByUserID(userID)
            .orElseThrow(()-> new UserNotFoundException("User " + userID + " does not found!"));
        return userVerification;
    }
    @Override
    public UserProfile getUserProfileByUsername(String username) {
        log.info("getUserProfileByUsername(username={})", username);
        User user = userService.getUser(username);
        return this.getUserProfile(user.getUserID());
    }
}
