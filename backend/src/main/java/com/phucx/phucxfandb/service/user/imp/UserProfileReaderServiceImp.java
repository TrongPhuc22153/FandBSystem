package com.phucx.phucxfandb.service.user.imp;

import com.phucx.phucxfandb.dto.response.UserProfileDTO;
import com.phucx.phucxfandb.entity.User;
import com.phucx.phucxfandb.mapper.UserProfileMapper;
import com.phucx.phucxfandb.repository.UserRepository;
import com.phucx.phucxfandb.service.user.UserProfileReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileReaderServiceImp implements UserProfileReaderService {
    private final UserRepository userRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    public UserProfileDTO getUserProfileByUserId(String userID){
        log.info("getUserProfile(userID={})", userID);
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new UsernameNotFoundException("User " + userID + " is not found"));
        return userProfileMapper.toUserProfileDTO(user.getProfile());
    }

    @Override
    public UserProfileDTO getUserProfileByUsername(String username){
        log.info("getUserProfileByUsername(username={})", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " is not found"));
        return userProfileMapper.toUserProfileDTO(user.getProfile());
    }
}
