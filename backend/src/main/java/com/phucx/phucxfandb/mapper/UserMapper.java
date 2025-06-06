package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RegisterUserDTO;
import com.phucx.phucxfandb.dto.response.UserDTO;
import com.phucx.phucxfandb.entity.Role;
import com.phucx.phucxfandb.entity.User;
import com.phucx.phucxfandb.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "image", source = "user.profile.picture")
    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(role -> role.getRoleName().name()).collect(java.util.stream.Collectors.toSet()))")
    UserDTO toUserDTO(User user);

    @Named("toBriefUserDTO")
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserDTO toBriefUserDTO(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "profile", source = "profile")
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User toUser(RegisterUserDTO registerUserDTO, List<Role> roles, UserProfile profile);
}
