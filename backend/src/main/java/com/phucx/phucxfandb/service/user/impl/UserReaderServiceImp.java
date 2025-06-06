package com.phucx.phucxfandb.service.user.impl;


import com.phucx.phucxfandb.dto.request.UserRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.UserDTO;
import com.phucx.phucxfandb.entity.User;
import com.phucx.phucxfandb.entity.UserProfile;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.UserMapper;
import com.phucx.phucxfandb.repository.UserRepository;
import com.phucx.phucxfandb.service.image.ImageReaderService;
import com.phucx.phucxfandb.service.user.UserReaderService;
import com.phucx.phucxfandb.specifications.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserReaderServiceImp implements UserReaderService {
    private final UserRepository userRepository;
    private final ImageReaderService imageReaderService;
    private final UserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public User getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName(), "email", email));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserEntityByUserId(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName(), "id", userId));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName(), "username", username));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByUserId(String userId) {
        return userRepository.findById(userId)
                .map(this::setUserImage)
                .map(mapper::toUserDTO)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName(), "id", userId));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::setUserImage)
                .map(mapper::toUserDTO)
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName(), "username", username));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getUsers(UserRequestParamsDTO params) {
        Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), Sort.by(params.getDirection(), params.getField()));
        Specification<User> spec = Specification.where(UserSpecification.hasRole(params.getRoleName()))
                .and(UserSpecification.hasUsername(params.getUsername()))
                .and(UserSpecification.isEnabled(params.getEnabled()))
                .and(UserSpecification.hasEmail(params.getEmail()))
                .and(UserSpecification.hasSearch(params.getSearch()));
        return userRepository.findAll(spec, pageable)
                .map(this::setUserImage)
                .map(mapper::toUserDTO);
    }

    private User setUserImage(User user){
        UserProfile profile = user.getProfile();
        if(!(profile.getPicture()==null || profile.getPicture().isEmpty())){
            String imageUrl = imageReaderService.getImageUrl(profile.getPicture());
            profile.setPicture(imageUrl);
        }
        return user;
    }
}
