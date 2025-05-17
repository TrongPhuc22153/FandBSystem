package com.phucx.phucxfandb.service.user.imp;

import com.phucx.phucxfandb.dto.request.RequestUserProfileDTO;
import com.phucx.phucxfandb.dto.response.UserProfileDTO;
import com.phucx.phucxfandb.entity.UserProfile;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.UserProfileMapper;
import com.phucx.phucxfandb.repository.UserProfileRepository;
import com.phucx.phucxfandb.service.image.ImageUpdateService;
import com.phucx.phucxfandb.service.user.UserProfileUpdateService;
import com.phucx.phucxfandb.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileUpdateServiceImpl implements UserProfileUpdateService {
    private final UserProfileRepository userProfileRepository;
    private final ImageUpdateService imageUpdateService;
    private final UserProfileMapper userProfileMapper;

    @Override
    @Transactional
    public UserProfileDTO updateUserProfile(String username, RequestUserProfileDTO requestUserProfileDTO) {
        UserProfile existingProfile = userProfileRepository.findByUserUsername(username)
                .orElseThrow(()-> new NotFoundException(UserProfile.class.getName(), "username", username));

        updateImage(requestUserProfileDTO, existingProfile.getPicture());
        userProfileMapper.updateUserProfile(requestUserProfileDTO, existingProfile);

        UserProfile updatedProfile = userProfileRepository.save(existingProfile);
        return userProfileMapper.toUserProfileDTO(updatedProfile);
    }

    private void updateImage(RequestUserProfileDTO requestUserProfileDTO, String existingImage){
        if(requestUserProfileDTO.getPicture()!=null && !requestUserProfileDTO.getPicture().isEmpty()){
            String newImageName = ImageUtils.extractImageNameFromUrl(requestUserProfileDTO.getPicture());
            if(existingImage != null){
                if(!newImageName.equalsIgnoreCase(existingImage)){
                    imageUpdateService.removeImages(List.of(existingImage));
                    requestUserProfileDTO.setPicture(newImageName);
                }else{
                    requestUserProfileDTO.setPicture(existingImage);
                }
            }else{
                requestUserProfileDTO.setPicture(newImageName);
            }
        }
    }
}
