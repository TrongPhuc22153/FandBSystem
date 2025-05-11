package com.phucx.phucxfandb.service.user;


import com.phucx.phucxfandb.dto.request.RequestUserProfileDTO;
import com.phucx.phucxfandb.dto.response.UserProfileDTO;

public interface UserProfileUpdateService {
    UserProfileDTO updateUserProfile(String username, RequestUserProfileDTO requestUserProfileDTO);

}
