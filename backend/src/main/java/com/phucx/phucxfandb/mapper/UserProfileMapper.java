package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.response.UserProfileDTO;
import com.phucx.phucxfandb.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileDTO toUserProfileDTO(UserProfile userProfile);
}
