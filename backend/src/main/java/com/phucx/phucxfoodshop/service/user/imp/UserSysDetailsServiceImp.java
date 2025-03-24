package com.phucx.phucxfoodshop.service.user.imp;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.phucx.phucxfoodshop.exceptions.UserAuthenticationException;
import com.phucx.phucxfoodshop.model.User;
import com.phucx.phucxfoodshop.model.UserRegisterInfo;
import com.phucx.phucxfoodshop.model.UserSysDetails;
import com.phucx.phucxfoodshop.repository.UserRepository;
import com.phucx.phucxfoodshop.service.user.UserSysDetailsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserSysDetailsServiceImp implements UserSysDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()->
            new UsernameNotFoundException("User " + username + " does not found!")
        );
        List<GrantedAuthority> role = this.getRole(username);
        return UserSysDetails.builder()
            .user(user)
            .role(role)
            .build();
    }

    @Override
	public Boolean checkUserAuthentication(String password, String hashedPasswored) {
        return passwordEncoder.matches(password, hashedPasswored);
	}

    @Override
    public Boolean checkPassword(String hashedPassword, String rawPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    private List<GrantedAuthority> getRole(String username){
        return userRepository.getUserRoles(username).stream()
            .map(x -> new SimpleGrantedAuthority("ROLE_"+x.toUpperCase()))
            .collect(Collectors.toList());
    }

    @Override
    public Boolean registerEmployee(UserRegisterInfo userRegisterInfo) throws UserAuthenticationException {
        log.info("registerEmployee(username={})", userRegisterInfo.getUsername());
        String password = userRegisterInfo.getPassword();
        String confirmPassword = userRegisterInfo.getConfirmPassword();
        String username = userRegisterInfo.getUsername();
        String firstname = userRegisterInfo.getFirstname();
        String lastname = userRegisterInfo.getLastname();
        String email = userRegisterInfo.getEmail();
        if(!confirmPassword.equals(password)){
            throw new UserAuthenticationException("Password and confirm password do match!");
        }
        Optional<User> optional = this.userRepository.findByUsername(userRegisterInfo.getUsername());
        if(optional.isPresent()){
            throw new UserAuthenticationException("User " + username + " already exists!");
        }
        Optional<User> emailOptional = this.userRepository.findByEmail(userRegisterInfo.getEmail());
        if(emailOptional.isPresent()){
            throw new UserAuthenticationException("Email " + userRegisterInfo.getEmail() + " already exists!");
        }
        String userID = UUID.randomUUID().toString();
        String employeeID = UUID.randomUUID().toString();
        String profileID = UUID.randomUUID().toString();
        String hashedPassword = this.passwordEncoder.encode(password);
        Boolean status = this.userRepository.createEmployeeUser(
            userID, employeeID, profileID, firstname, lastname, 
            email, username, hashedPassword);
        return status;
    }

    @Override
	public Boolean registerCustomer(UserRegisterInfo userRegisterInfo) throws UserAuthenticationException {
        log.info("registerCustomer(username={})", userRegisterInfo.getUsername());
        String password = userRegisterInfo.getPassword();
        String confirmPassword = userRegisterInfo.getConfirmPassword();
        String username = userRegisterInfo.getUsername();
        String firstname = userRegisterInfo.getFirstname();
        String lastname = userRegisterInfo.getLastname();
        String email = userRegisterInfo.getEmail();
        if(!confirmPassword.equals(password)){
            throw new UserAuthenticationException("Password and confirm password do match!");
        }
        Optional<User> optional = this.userRepository.findByUsername(userRegisterInfo.getUsername());
        if(optional.isPresent()){
            throw new UserAuthenticationException("User " + username + " already exists!");
        }
        Optional<User> emailOptional = this.userRepository.findByEmail(userRegisterInfo.getEmail());
        if(emailOptional.isPresent()){
            throw new UserAuthenticationException("Email " + userRegisterInfo.getEmail() + " already exists!");
        }
        String userID = UUID.randomUUID().toString();
        String customerID = UUID.randomUUID().toString();
        String profileID = UUID.randomUUID().toString();
        String hashedPassword = this.passwordEncoder.encode(password);
        Boolean status = this.userRepository.createCustomerUser(
            userID, customerID, profileID, firstname, lastname, 
            email, username, hashedPassword);
        return status;
	}

    @Override
    public Boolean updateUserPassword(String userID, String password) {
        log.info("updateUserPassword(userID={})", userID);
        String hashedPassword = passwordEncoder.encode(password);
        return userRepository.updateUserPassword(userID, hashedPassword);
    }
}
