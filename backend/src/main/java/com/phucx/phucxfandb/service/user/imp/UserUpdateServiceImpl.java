package com.phucx.phucxfandb.service.user.imp;

import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.dto.request.RequestUserDTO;
import com.phucx.phucxfandb.dto.response.UserDTO;
import com.phucx.phucxfandb.entity.*;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.UserMapper;
import com.phucx.phucxfandb.repository.UserRepository;
import com.phucx.phucxfandb.service.role.RoleReaderService;
import com.phucx.phucxfandb.service.user.UserUpdateService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserUpdateServiceImpl implements UserUpdateService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleReaderService roleReaderService;
    private final UserMapper mapper;

    @Override
    @Modifying
    @Transactional
    public UserDTO createUser(RequestUserDTO requestUserDTO) {
        log.info("createUser(username={}, email={}, role={})",
                requestUserDTO.getUsername(), requestUserDTO.getEmail(), requestUserDTO.getRoles());
        if(userRepository.findByUsername(requestUserDTO.getUsername()).isPresent()){
            throw new EntityExistsException("User " + requestUserDTO.getUsername() + " already exists!");
        }
        if(userRepository.findByEmail(requestUserDTO.getEmail()).isPresent()){
            throw new EntityExistsException("User with email " + requestUserDTO.getEmail() + " already exists!");
        }
        String firstName = requestUserDTO.getFirstName();
        String lastName = requestUserDTO.getLastName();
        // Create user
        User newUser = new User();
        newUser.setUsername(requestUserDTO.getUsername());
        newUser.setEnabled(true);
        newUser.setPassword(passwordEncoder.encode(requestUserDTO.getPassword()));
        newUser.setEmail(requestUserDTO.getEmail());
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        // Add roles
        Set<RoleName> roles = new HashSet<>(requestUserDTO.getRoles());
        Set<Role> roleEntities = roleReaderService.getRoleEntitiesByName(roles);
        newUser.getRoles().addAll(roleEntities);
        // Add profile
        UserProfile profile = new UserProfile();
        profile.setIsDeleted(false);
        profile.setUser(newUser);
        newUser.setProfile(profile);
        if(roles.contains(RoleName.CUSTOMER)){
            // set Customer
            Customer customer = new Customer();
            customer.setContactName(firstName + " " + lastName);
            profile.setCustomer(customer);
            customer.setProfile(profile);
            profile.setCustomer(customer);
        }else{
            // Employee
            Employee employee = new Employee();
            profile.setEmployee(employee);
            employee.setProfile(profile);
            profile.setEmployee(employee);
        }

        User savedUser = userRepository.save(newUser);
        return mapper.toUserDTO(savedUser);
    }


    @Override
    @Modifying
    @Transactional
    public UserDTO updateUserEnabledStatus(String id, RequestUserDTO requestUserDTO) {
        log.info("updateUserEnabledStatus(id={}, requestUserDTO={})", id, requestUserDTO);
        User existingUser = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("User", "id", id));
        existingUser.setEnabled(requestUserDTO.getEnabled());
        User updated = userRepository.save(existingUser);
        return mapper.toUserDTO(updated);
    }
}
