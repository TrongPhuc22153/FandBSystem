package com.phucx.phucxfandb.service.user.imp;

import com.phucx.phucxfandb.dto.response.UserProfileDTO;
import com.phucx.phucxfandb.entity.UserProfile;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.UserProfileMapper;
import com.phucx.phucxfandb.repository.UserProfileRepository;
import com.phucx.phucxfandb.service.image.ImageReaderService;
import com.phucx.phucxfandb.service.user.UserProfileReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileReaderServiceImp implements UserProfileReaderService {
    private final UserProfileRepository userProfileRepository;
    private final ImageReaderService imageReaderService;
    private final UserProfileMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfileByUserId(String userID){
        log.info("getUserProfileByUserId(userID={})", userID);
        return userProfileRepository.findByUserUserId(userID)
                .map(this::setImageUrl)
                .map(mapper::toUserProfileDTO)
                .orElseThrow(() -> new NotFoundException(UserProfile.class.getSimpleName(), "id", userID));
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfileByUsername(String username){
        log.info("getUserProfileByUsername(username={})", username);
        return userProfileRepository.findByUserUsername(username)
                .map(this::setImageUrl)
                .map(mapper::toUserProfileDTO)
                .orElseThrow(() -> new NotFoundException(UserProfile.class.getSimpleName(), "username", username));
    }

    private UserProfile setImageUrl(UserProfile profile){
        if(!(profile.getPicture()==null || profile.getPicture().isEmpty())){
            String imageUrl = imageReaderService.getImageUrl(profile.getPicture());
            profile.setPicture(imageUrl);
        }
        return profile;
    }
}
