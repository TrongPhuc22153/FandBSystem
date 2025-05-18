package com.phucx.phucxfandb.service.user;

import com.phucx.phucxfandb.dto.request.*;
import com.phucx.phucxfandb.dto.response.LoginResponse;
import com.phucx.phucxfandb.dto.response.LogoutResponseDTO;
import com.phucx.phucxfandb.dto.response.RegisteredUserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {

    RegisteredUserDTO registerCustomer(RegisterUserDTO registerUserDTO);
    RegisteredUserDTO registerEmployee(RegisterUserDTO registerUserDTO);
    RegisteredUserDTO registerAdmin(RegisterUserDTO registerUserDTO);

    LoginResponse signIn(LoginUserDTO loginUserDTO);
    LogoutResponseDTO signOut(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

    Boolean updateUserPassword(UpdateForgetPassword UpdateForgetPassword);
    Boolean resetUserPasswordRandom(String userID);
    void updateUserPassword(String username, UpdateUserPasswordDTO userChangePassword);
}
