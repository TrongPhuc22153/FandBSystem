package com.phucx.phucxfandb.service.user;

import com.phucx.phucxfandb.dto.request.RequestUserDTO;
import com.phucx.phucxfandb.dto.request.UpdateUserPasswordDTO;
import com.phucx.phucxfandb.dto.response.UserDTO;
import org.springframework.security.core.Authentication;

public interface UserUpdateService {
    UserDTO createUser(RequestUserDTO requestUserDTO);

    UserDTO updateUserEnabledStatus(String id, RequestUserDTO requestUserDTO);

    void updateUserPassword(Authentication authentication, UpdateUserPasswordDTO userChangePassword);
}
