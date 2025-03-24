package com.phucx.phucxfoodshop.service.user.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.constant.WebConstant;
import com.phucx.phucxfoodshop.exceptions.CustomerNotFoundException;
import com.phucx.phucxfoodshop.exceptions.EmployeeNotFoundException;
import com.phucx.phucxfoodshop.exceptions.UserNotFoundException;
import com.phucx.phucxfoodshop.model.User;
import com.phucx.phucxfoodshop.model.UserDetails;
import com.phucx.phucxfoodshop.repository.UserDetailsRepository;
import com.phucx.phucxfoodshop.repository.UserRepository;
import com.phucx.phucxfoodshop.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private UserRepository userRepository;

	@Override
	public User getUser(String username) throws UserNotFoundException {
        log.info("getUser(username={})", username);
        return userRepository.findByUsername(username)
            .orElseThrow(()-> new UserNotFoundException("User " + username + " does not found!"));
	}
	
    @Override
    public User getUserById(String userId) throws UserNotFoundException {
        log.info("getUserById(userId={})", userId);
        return userRepository.findById(userId).orElseThrow(
            ()-> new UserNotFoundException("User " + userId + " does not found!")
        );
    }

    @Override
    public Boolean updateEmailVerification(String username, Boolean status) {
        log.info("updateEmailVerification(username={}, status={})", username, status);
        return userRepository.updateEmailVerification(username, status);
    }
    @Override
    public Page<UserDetails> getUsersByRole(String rolename, Integer pageNumber) {
        log.info("getUsersByRole(rolename={})", rolename);
        Pageable pageable = PageRequest.of(pageNumber, WebConstant.PAGE_SIZE);
        return userDetailsRepository.findByRoleName(rolename, pageable);
    }

    @Override
    public User getUserByEmail(String email) {
        log.info("getUserByEmail(email={})", email);
        return  userRepository.findByEmail(email).orElseThrow(
            ()-> new UserNotFoundException("Email " + email + " does not found")
        );
    }
    
    @Override
    public User getUserByCustomerID(String customerID) {
        log.info("getUserByCustomerID(customerID={})", customerID);
        return userRepository.findByCustomerID(customerID).orElseThrow(
            ()-> new CustomerNotFoundException("Customer " + customerID + " does not found!")
        );
    }

    @Override
    public User getUserByEmployeeID(String employeeID) {
        log.info("getUserByEmployeeID(employeeID={})", employeeID);
        return userRepository.findByEmployeeID(employeeID).orElseThrow(
            ()-> new EmployeeNotFoundException("Employee " + employeeID + " does not found!")
        );
    }

    @Override
    public Page<UserDetails> getUsersByRoleAndUsernameLike(String rolename, String username, Integer pageNumber) {
        log.info("getUsersByRoleAndUsernameLike(rolename={}, username={}, pageNumber={})", rolename, username, pageNumber);
        Pageable pageable = PageRequest.of(pageNumber, WebConstant.PAGE_SIZE);
        String searchValue = "%" + username + "%";
        return userDetailsRepository.findByUsernameLikeAndRolename(searchValue, rolename, pageable);
    }
    @Override
    public Page<UserDetails> getUsersByRoleAndFirstNameLike(String rolename, String firstName, Integer pageNumber) {
        log.info("getUsersByRoleAndFirstNameLike(rolename={}, firstName={}, pageNumber={})", rolename, firstName, pageNumber);
        Pageable pageable = PageRequest.of(pageNumber, WebConstant.PAGE_SIZE);
        String searchValue = "%" + firstName + "%";
        return userDetailsRepository.findByFirstNameLikeAndRolename(searchValue, rolename, pageable);
    }
    @Override
    public Page<UserDetails> getUsersByRoleAndLastNameLike(String rolename, String lastName, Integer pageNumber) {
        log.info("getUsersByRoleAndLastNameLike(rolename={}, lastName={}, pageNumber={})", rolename, lastName, pageNumber);
        Pageable pageable = PageRequest.of(pageNumber, WebConstant.PAGE_SIZE);
        String searchValue = "%" + lastName + "%";
        return userDetailsRepository.findByLastNameLikeAndRolename(searchValue, rolename, pageable);
    }
    @Override
    public Page<UserDetails> getUsersByRoleAndEmailLike(String rolename, String email, Integer pageNumber) {
        log.info("getUsersByRoleAndEmailLike(rolename={}, email={}, pageNumber={})", rolename, email, pageNumber);
        Pageable pageable = PageRequest.of(pageNumber, WebConstant.PAGE_SIZE);
        String searchValue = "%" + email + "%";
        return userDetailsRepository.findByEmailLikeAndRolename(searchValue, rolename, pageable);
    }
    
}
