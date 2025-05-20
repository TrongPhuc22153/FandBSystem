package com.phucx.phucxfandb.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.phucx.phucxfandb.constant.ValidationGroups;
import com.phucx.phucxfandb.constant.Views;
import com.phucx.phucxfandb.dto.request.*;
import com.phucx.phucxfandb.dto.response.*;
import com.phucx.phucxfandb.service.user.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth/", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication endpoint for users")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Login endpoint", description = "Public access")
    public ResponseEntity<ResponseDTO<LoginResponse>> signIn(
            @Valid @RequestBody LoginUserDTO loginUserDTO
    ){
        LoginResponse loginResponse = authenticationService.signIn(loginUserDTO);
        ResponseDTO<LoginResponse> responseDTO = ResponseDTO.<LoginResponse>builder()
                .message("Login successfully")
                .data(loginResponse)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Customer register endpoint", description = "Public access")
    public ResponseEntity<ResponseDTO<RegisteredUserDTO>> signUp(
            @Valid @RequestBody RegisterUserDTO registerUserDTO
    ){
        RegisteredUserDTO registeredUserDTO = authenticationService.registerCustomer(registerUserDTO);
        ResponseDTO<RegisteredUserDTO> responseDTO = ResponseDTO.<RegisteredUserDTO>builder()
                .message("User registered successfully")
                .data(registeredUserDTO)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping(value = "/logout")
    @Operation(summary = "Logout endpoint", description = "Authenticated access")
    public ResponseEntity<ResponseDTO<LogoutResponseDTO>> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ){
        var logoutResponse = authenticationService
                .signOut(request, response, authentication);

        ResponseDTO<LogoutResponseDTO> responseDTO = ResponseDTO.<LogoutResponseDTO>builder()
                .message("Logout successful")
                .data(logoutResponse)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping(value = "/forgot")
    @Operation(summary = "Forgot password endpoint", description = "Public access")
    public ResponseEntity<ResponseDTO<Void>> forgotPassword(
            @JsonView(Views.ForgetPassword.class)
            @Validated(ValidationGroups.ForgetPassword.class)
            @RequestBody RequestForgotPasswordDTO requestForgotPasswordDTO){
        authenticationService.forgotPassword(requestForgotPasswordDTO);

        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message("An email is sent to your mail")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/validate")
    @Operation(summary = "Validate token endpoint", description = "Public access")
    public ResponseEntity<ResponseDTO<ValidatedTokenResponseDTO>> validateToken(
            @JsonView(Views.ValidateToken.class)
            @Validated(ValidationGroups.ValidateToken.class)
            @RequestBody RequestForgotPasswordDTO requestForgotPasswordDTO){
        authenticationService.validateTokenPassword(requestForgotPasswordDTO);

        ValidatedTokenResponseDTO validatedTokenResponseDTO = ValidatedTokenResponseDTO.builder()
                .status(Boolean.TRUE)
                .build();

        ResponseDTO<ValidatedTokenResponseDTO> response = ResponseDTO.<ValidatedTokenResponseDTO>builder()
                .message("Your token is valid")
                .data(validatedTokenResponseDTO)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/reset")
    @Operation(summary = "Reset password endpoint", description = "Public access")
    public ResponseEntity<ResponseDTO<Object>> resetPassword(
            @JsonView(Views.ResetPassword.class)
            @Validated(ValidationGroups.ResetPassword.class)
            @RequestBody RequestForgotPasswordDTO requestForgotPasswordDTO){
        ResponseDTO<Object> response = authenticationService
                .updateUserPassword(requestForgotPasswordDTO);
        return ResponseEntity.ok().body(response);
    }
}
