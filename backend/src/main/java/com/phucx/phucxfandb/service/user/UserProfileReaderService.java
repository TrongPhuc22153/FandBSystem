package com.phucx.phucxfandb.service.user;

import com.phucx.phucxfandb.dto.response.UserProfileDTO;

public interface UserProfileReaderService {
    UserProfileDTO getUserProfileByUserId(String userID);
    UserProfileDTO getUserProfileByUsername(String username);
}
