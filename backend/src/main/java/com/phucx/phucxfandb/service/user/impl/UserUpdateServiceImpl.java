package com.phucx.phucxfandb.service.user.impl;

import com.phucx.phucxfandb.enums.RoleName;
import com.phucx.phucxfandb.dto.request.RequestUserDTO;
import com.phucx.phucxfandb.dto.request.UpdateUserPasswordDTO;
import com.phucx.phucxfandb.dto.response.UserDTO;
import com.phucx.phucxfandb.entity.*;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.UserMapper;
import com.phucx.phucxfandb.repository.UserRepository;
import com.phucx.phucxfandb.service.email.EmailService;
import com.phucx.phucxfandb.service.role.RoleReaderService;
import com.phucx.phucxfandb.service.user.UserUpdateService;
import com.phucx.phucxfandb.utils.PasswordGeneratorUtils;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserUpdateServiceImpl implements UserUpdateService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleReaderService roleReaderService;
    private final EmailService emailService;
    private final UserMapper mapper;

    @Override
    @Transactional
    public UserDTO createUser(RequestUserDTO requestUserDTO) {
        String username = requestUserDTO.getUsername();
        String email = requestUserDTO.getEmail();

        if(userRepository.existsByUsername(username)){
            throw new EntityExistsException(String.format("User with username %s already existed!", username));
        }
        if(userRepository.existsByEmail(email)){
            throw new EntityExistsException(String.format("User with email %s already existed!", email));
        }
        String firstName = requestUserDTO.getFirstName();
        String lastName = requestUserDTO.getLastName();

        User newUser = new User();
        newUser.setResetPassword(Boolean.TRUE);
        newUser.setUsername(username);
        newUser.setEnabled(true);
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);

        String randomPassword = PasswordGeneratorUtils.generatePassword(PasswordGeneratorUtils.DEFAULT_PASSWORD_LENGTH);
        newUser.setPassword(passwordEncoder.encode(randomPassword));

        Set<RoleName> roles = Set.copyOf(requestUserDTO.getRoles());
        Set<Role> roleEntities = roleReaderService.getRoleEntitiesByName(roles);
        newUser.getRoles().addAll(roleEntities);

        UserProfile profile = new UserProfile();
        profile.setIsDeleted(false);
        profile.setUser(newUser);
        newUser.setProfile(profile);
        if(roles.contains(RoleName.CUSTOMER)){
            Customer customer = new Customer();
            customer.setContactName(firstName + " " + lastName);
            customer.setProfile(profile);
            profile.setCustomer(customer);
        }else{
            Employee employee = new Employee();
            employee.setProfile(profile);
            profile.setEmployee(employee);
        }

        User savedUser = userRepository.save(newUser);
        if(savedUser.getUserId()!=null){
            emailService.sendPassword(email, firstName, lastName, username, randomPassword);
        }
        return mapper.toUserDTO(savedUser);
    }


    @Override
    @Transactional
    public UserDTO updateUserEnabledStatus(String id, RequestUserDTO requestUserDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(User.class.getSimpleName(), "id", id));
        existingUser.setEnabled(requestUserDTO.getEnabled());
        User updated = userRepository.save(existingUser);
        return mapper.toUserDTO(updated);
    }

    @Override
    @Transactional
    public void updateUserPassword(Authentication authentication, UpdateUserPasswordDTO userChangePassword) {
        String username = authentication.getName();
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName(), "username", username));

        String oldPassword = userChangePassword.getPassword();
        String newPassword = userChangePassword.getNewPassword();

        if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect.");
        }

        if (passwordEncoder.matches(newPassword, existingUser.getPassword())) {
            throw new IllegalArgumentException("New password must be different from the old password.");
        }

        existingUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(existingUser);
    }
}
