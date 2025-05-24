package com.phucx.phucxfandb.service.user.impl;

import com.phucx.phucxfandb.constant.EmailConstants;
import com.phucx.phucxfandb.constant.JwtType;
import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.dto.request.*;
import com.phucx.phucxfandb.dto.response.LoginResponse;
import com.phucx.phucxfandb.dto.response.LogoutResponseDTO;
import com.phucx.phucxfandb.dto.response.RegisteredUserDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.entity.*;
import com.phucx.phucxfandb.exception.EntityExistsException;
import com.phucx.phucxfandb.exception.InvalidTokenException;
import com.phucx.phucxfandb.repository.UserRepository;
import com.phucx.phucxfandb.service.email.EmailService;
import com.phucx.phucxfandb.service.jwt.JwtAuthenticationService;
import com.phucx.phucxfandb.service.jwt.JwtEmailService;
import com.phucx.phucxfandb.service.role.RoleReaderService;
import com.phucx.phucxfandb.service.user.AuthenticationService;
import com.phucx.phucxfandb.utils.RandomStringGeneratorUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final JwtEmailService jwtEmailService;
    private final RoleReaderService roleReaderService;
    private final EmailService emailService;

    @Override
    @Transactional
    public RegisteredUserDTO registerEmployee(RegisterUserDTO registerUserDTO){
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
        user.setEmailVerified(Boolean.TRUE);
        Role role = roleReaderService.getRoleEntityByName(RoleName.EMPLOYEE);
        user.getRoles().add(role);
        UserProfile profile = new UserProfile();
        user.setProfile(profile);
        profile.setUser(user);
        Employee employee = new Employee();
        employee.setHireDate(LocalDate.now());
        profile.setEmployee(employee);
        employee.setProfile(profile);

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
    @Transactional
    public RegisteredUserDTO registerAdmin(RegisterUserDTO registerUserDTO) {
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
        user.setEmailVerified(Boolean.TRUE);
        List<Role> roles = roleReaderService.getRoleEntitiesByName(
                List.of(RoleName.EMPLOYEE, RoleName.ADMIN)
        );
        user.getRoles().addAll(roles);
        UserProfile profile = new UserProfile();
        user.setProfile(profile);
        profile.setUser(user);
        Employee employee = new Employee();
        profile.setEmployee(employee);
        employee.setProfile(profile);
        profile.setEmployee(employee);
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
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginUserDTO.getUsername(),
                loginUserDTO.getPassword()
        );
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        User user = userRepository.findByUsername(loginUserDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username " + loginUserDTO.getUsername() + " not found!"));

        String accessToken;
        if(!user.getResetPassword()){
            accessToken = jwtAuthenticationService.generateAuthToken(user);
        }else{
            accessToken = jwtAuthenticationService.generateResetPasswordToken(user);
        }

        return LoginResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .accessToken(accessToken)
                .build();
    }

    @Override
    public LogoutResponseDTO signOut(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String username = authentication.getName();
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
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
    @Transactional
	public RegisteredUserDTO registerCustomer(RegisterUserDTO registerUserDTO){
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
        user.setEmailVerified(Boolean.TRUE);
        Role role = roleReaderService.getRoleEntityByName(RoleName.CUSTOMER);
        user.getRoles().add(role);
        UserProfile profile = new UserProfile();
        user.setProfile(profile);
        profile.setUser(user);
        Customer customer = new Customer();
        customer.setContactName(firstName + " " + lastName);
        profile.setCustomer(customer);
        customer.setProfile(profile);
        profile.setCustomer(customer);
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
    @Transactional
    public Boolean resetUserPasswordRandom(String userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User " + userId + " is not found!"));
        String password = RandomStringGeneratorUtils.generateRandomString(20);
        String text = "Your new password is: " + password;
        emailService.sendMessage(existingUser.getEmail(), EmailConstants.RESET_PASSWORD_SUBJECT, text);

        existingUser.setPassword(passwordEncoder.encode(password));
        User updatedUser = userRepository.save(existingUser);
        return passwordEncoder.matches(password, updatedUser.getPassword());
    }

    @Override
    @Transactional
    public ResponseDTO<Object> updateUserPassword(RequestForgotPasswordDTO requestForgotPasswordDTO) {
        String token = requestForgotPasswordDTO.getToken();

        JwtType type = jwtAuthenticationService.extractType(token);

        User user;
        switch (type){
            case RESET_PASSWORD_EMAIL -> {
                jwtEmailService.validateToken(token, JwtType.RESET_PASSWORD_EMAIL);
                String email = jwtEmailService.extractEmail(token);
                user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", email)));
                String hashedPassword = passwordEncoder.encode(requestForgotPasswordDTO.getNewPassword());
                user.setPassword(hashedPassword);
                userRepository.save(user);

                return ResponseDTO.builder()
                        .message("Your password updated successfully")
                        .build();
            }

            case RESET_PASSWORD -> {
                jwtAuthenticationService.validateResetToken(token);
                String username = jwtAuthenticationService.extractUsername(token);
                user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s not found", username)));
                user.setResetPassword(Boolean.FALSE);
                String hashedPassword = passwordEncoder.encode(requestForgotPasswordDTO.getNewPassword());
                user.setPassword(hashedPassword);
                User savedUser = userRepository.save(user);

                String accessToken = jwtAuthenticationService.generateAuthToken(savedUser);
                LoginResponse tokenResponse = LoginResponse.builder()
                        .username(username)
                        .accessToken(accessToken)
                        .build();

                return ResponseDTO.builder()
                        .message("Your password updated successfully")
                        .data(tokenResponse)
                        .build();
            }

            default ->{
                throw new InvalidTokenException("Invalid token");
            }
        }
    }

    @Override
    public void forgotPassword(RequestForgotPasswordDTO requestForgotPasswordDTO) {
        emailService.sendResetPassword(requestForgotPasswordDTO.getEmail());
    }


    @Override
    public void validateTokenPassword(RequestForgotPasswordDTO requestForgotPasswordDTO) {
        String token = requestForgotPasswordDTO.getToken();
        jwtEmailService.validateToken(token, JwtType.RESET_PASSWORD_EMAIL);
    }
}
