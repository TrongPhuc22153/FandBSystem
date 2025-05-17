package com.phucx.phucxfandb.mapper;

import com.phucx.phucxfandb.dto.request.RequestUserProfileDTO;
import com.phucx.phucxfandb.dto.response.UserProfileDTO;
import com.phucx.phucxfandb.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserProfileMapper {
    @Mapping(target = "user", qualifiedByName = "toBriefUserDTO")
    UserProfileDTO toUserProfileDTO(UserProfile userProfile);

    @Mapping(target = "profileId", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateUserProfile(RequestUserProfileDTO requestUserProfileDTO, @MappingTarget UserProfile profile);
}
