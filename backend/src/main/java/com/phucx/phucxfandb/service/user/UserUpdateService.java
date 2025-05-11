package com.phucx.phucxfandb.service.user;

import com.phucx.phucxfandb.dto.request.RequestUserDTO;
import com.phucx.phucxfandb.dto.response.UserDTO;

public interface UserUpdateService {
    UserDTO createUser(RequestUserDTO requestUserDTO);
    UserDTO updateUserEnabledStatus(String id, RequestUserDTO requestUserDTO);
}
