package com.phucx.phucxfandb.service.user.imp;

import com.phucx.phucxfandb.constant.EmailVerified;
import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.dto.request.*;
import com.phucx.phucxfandb.dto.response.LoginResponse;
import com.phucx.phucxfandb.dto.response.LogoutResponseDTO;
import com.phucx.phucxfandb.dto.response.RegisteredUserDTO;
import com.phucx.phucxfandb.entity.*;
import com.phucx.phucxfandb.exception.EntityExistsException;
import com.phucx.phucxfandb.repository.UserRepository;
import com.phucx.phucxfandb.service.email.EmailService;
import com.phucx.phucxfandb.service.jwt.JwtAuthenticationService;
import com.phucx.phucxfandb.service.role.RoleReaderService;
import com.phucx.phucxfandb.service.user.AuthenticationService;
import com.phucx.phucxfandb.utils.RandomStringGeneratorUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final RoleReaderService roleReaderService;
    private final EmailService emailService;

//    private final Integer PASSWORD_LENGTH = 10;
//    private final Long RESET_TOKEN_TIME = 300000L;
//    private final String PASSWORD_RESET_SUBJECT = "PASSWORD RESET";
//    private final String RESET_URI = "/auth/reset";


    @Override
    @Modifying
    @Transactional
    public RegisteredUserDTO registerEmployee(RegisterUserDTO registerUserDTO){
        log.info("registerEmployee(username={})", registerUserDTO.getUsername());
        // Extract DTO fields
        String username = registerUserDTO.getUsername();
        String email = registerUserDTO.getEmail();
        String password = registerUserDTO.getPassword();
        String firstName = registerUserDTO.getFirstName();
        String lastName = registerUserDTO.getLastName();

        // Validate uniqueness
        if (userRepository.existsByUsername(username)) {
            throw new EntityExistsException("Username " + username + " already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new EntityExistsException("Email " + email + " already exists");
        }

        // Create User entity
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(Boolean.TRUE);
        user.setEmailVerified(EmailVerified.YES.getValue());
        // Role
        Role role = roleReaderService.getRoleEntityByName(RoleName.EMPLOYEE);
        user.getRoles().add(role);
        // Profile
        UserProfile profile = new UserProfile();
        user.setProfile(profile);
        profile.setUser(user);
        // Employee
        Employee employee = new Employee();
        employee.setHireDate(LocalDate.now());
        profile.setEmployee(employee);
        employee.setProfile(profile);

        // Save and return the user
        User savedUser = userRepository.save(user);
        return RegisteredUserDTO.builder()
                .username(username)
                .email(email)
                .registeredAt(LocalDateTime.now())
                .userId(savedUser.getUserId())
                .enabled(savedUser.getEnabled())
                .build();
    }

    @Override
    @Modifying
    @Transactional
    public RegisteredUserDTO registerAdmin(RegisterUserDTO registerUserDTO) {
        log.info("registerAdmin(username={})", registerUserDTO.getUsername());
        // Extract DTO fields
        String username = registerUserDTO.getUsername();
        String email = registerUserDTO.getEmail();
        String password = registerUserDTO.getPassword();
        String firstName = registerUserDTO.getFirstName();
        String lastName = registerUserDTO.getLastName();

        // Validate uniqueness
        if (userRepository.existsByUsername(username)) {
            throw new EntityExistsException("Username " + username + " already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new EntityExistsException("Email " + email + " already exists");
        }

        // Create User entity
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(Boolean.TRUE);
        user.setEmailVerified(EmailVerified.YES.getValue());
        // Role
        List<Role> roles = roleReaderService.getRoleEntitiesByName(
                List.of(RoleName.EMPLOYEE, RoleName.ADMIN)
        );
        user.getRoles().addAll(roles);
        // Profile
        UserProfile profile = new UserProfile();
        user.setProfile(profile);
        profile.setUser(user);
        // Employee
        Employee employee = new Employee();
        profile.setEmployee(employee);
        employee.setProfile(profile);
        profile.setEmployee(employee);
        // Save and return the user
        User savedUser = userRepository.save(user);
        return RegisteredUserDTO.builder()
                .username(username)
                .email(email)
                .registeredAt(LocalDateTime.now())
                .userId(savedUser.getUserId())
                .enabled(savedUser.getEnabled())
                .build();
    }

    @Override
    public LoginResponse signIn(LoginUserDTO loginUserDTO) {
        log.info("signIn(username={})", loginUserDTO.getUsername());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginUserDTO.getUsername(),
                loginUserDTO.getPassword()
        );
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        User user = userRepository.findByUsername(loginUserDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username " + loginUserDTO.getUsername() + " not found!"));

        String accessToken = jwtAuthenticationService.generateAuthToken(user);

        return LoginResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .accessToken(accessToken)
                .build();
    }

    @Override
    public LogoutResponseDTO signOut(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String username = authentication.getName();
        log.info("signOut(username={})", username);
        // Clear security context
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
        // Clear cookie
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return LogoutResponseDTO.builder()
                .username(username)
                .logoutAt(LocalDateTime.now())
                .build();
    }

    @Override
    @Modifying
    @Transactional
	public RegisteredUserDTO registerCustomer(RegisterUserDTO registerUserDTO){
        log.info("registerCustomer(username={})", registerUserDTO.getUsername());
        String username = registerUserDTO.getUsername();
        String email = registerUserDTO.getEmail();
        String password = registerUserDTO.getPassword();
        String firstName = registerUserDTO.getFirstName();
        String lastName = registerUserDTO.getLastName();

        if (userRepository.existsByUsername(username)) {
            throw new EntityExistsException("Username " + username + " already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new EntityExistsException("Email " + email + " already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(Boolean.TRUE);
        user.setEmailVerified(EmailVerified.YES.getValue());
        // set Role
        Role role = roleReaderService.getRoleEntityByName(RoleName.CUSTOMER);
        user.getRoles().add(role);
        // set profile
        UserProfile profile = new UserProfile();
        user.setProfile(profile);
        profile.setUser(user);
        // set Customer
        Customer customer = new Customer();
        customer.setContactName(firstName + " " + lastName);
        profile.setCustomer(customer);
        customer.setProfile(profile);
        profile.setCustomer(customer);
        // Save and return the user
        User savedUser = userRepository.save(user);
        return RegisteredUserDTO.builder()
                .username(username)
                .email(email)
                .registeredAt(LocalDateTime.now())
                .userId(savedUser.getUserId())
                .enabled(savedUser.getEnabled())
                .build();
	}

    @Override
    @Modifying
    @Transactional
    public Boolean updateUserPassword(UpdateForgetPassword UpdateForgetPassword) {
        log.info("updateUserPassword(userId={})", UpdateForgetPassword.getUserId());
        User user = userRepository.findById(UpdateForgetPassword.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User does not found!"));
        String hashedPassword = passwordEncoder.encode(UpdateForgetPassword.getPassword());
        user.setPassword(hashedPassword);
        User updatedUser = userRepository.save(user);
        return passwordEncoder.matches(updatedUser.getPassword(), hashedPassword);
    }

    @Override
    @Modifying
    @Transactional
    public Boolean resetUserPasswordRandom(String userId) {
        log.info("resetUserPasswordRandom(userId={})", userId);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User " + userId + " is not found!"));
        String password = RandomStringGeneratorUtils.generateRandomString(20);
        String text = "Your new password is: " + password;
        emailService.sendMessage(existingUser.getEmail(), EmailService.PASSWORD_RESET_SUBJECT, text);

        existingUser.setPassword(passwordEncoder.encode(password));
        User updatedUser = userRepository.save(existingUser);
        return passwordEncoder.matches(password, updatedUser.getPassword());
    }

    @Override
    @Modifying
    @Transactional
    public Boolean changePassword(UpdateUserPasswordDTO userChangePassword){
        log.info("changePassword(userId={}, email={})", userChangePassword.getUserId(), userChangePassword.getEmail());

        User existingUser = userRepository.findById(userChangePassword.getUserId())
                .orElseThrow(()-> new UsernameNotFoundException("User " + userChangePassword.getUserId() + " is not found!"));

        String password = userChangePassword.getPassword();
        String newPassword = userChangePassword.getNewPassword();
        // check password
        if(newPassword.equals(password))
//            throw new UserResetPasswordException("Your new password and old password can not match!");
        if(!userChangePassword.getEmail().equalsIgnoreCase(existingUser.getEmail()))
//            throw new UserResetPasswordException("Your email does not match with your original email!");
        if(!passwordEncoder.matches(password, existingUser.getPassword())){
//            throw new UserResetPasswordException("Your password does not match your old password!");
        }
        // update password
        existingUser.setPassword(passwordEncoder.encode(password));
        User updatedUser = userRepository.save(existingUser);
        return passwordEncoder.matches(password, updatedUser.getPassword());
    }
}
