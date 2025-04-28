package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RegisterUserDTO;
import com.phucx.phucxfandb.dto.response.UserDTO;
import com.phucx.phucxfandb.entity.Role;
import com.phucx.phucxfandb.entity.User;
import com.phucx.phucxfandb.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "profile", source = "profile")
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User toUser(RegisterUserDTO registerUserDTO, List<Role> roles, UserProfile profile);
}
