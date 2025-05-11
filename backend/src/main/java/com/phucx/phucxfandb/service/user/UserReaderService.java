package com.phucx.phucxfandb.service.user;

import com.phucx.phucxfandb.dto.request.UserRequestParamDTO;
import com.phucx.phucxfandb.dto.response.UserDTO;
import com.phucx.phucxfandb.entity.User;
import org.springframework.data.domain.Page;

public interface UserReaderService {
    User getUserEntityByUserId(String userId);
    User getUserEntityByUsername(String username);
    UserDTO getUserByUserId(String userId);
    UserDTO getUserByUsername(String username);
    Page<UserDTO> getUsers(UserRequestParamDTO params);
}
