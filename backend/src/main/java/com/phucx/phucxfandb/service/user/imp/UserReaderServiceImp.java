package com.phucx.phucxfandb.service.user.imp;


import com.phucx.phucxfandb.constant.RoleName;
import com.phucx.phucxfandb.dto.response.UserDTO;
import com.phucx.phucxfandb.entity.User;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.UserMapper;
import com.phucx.phucxfandb.repository.UserRepository;
import com.phucx.phucxfandb.service.user.UserReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserReaderServiceImp implements UserReaderService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public User getUserEntityByUserId(String userId) {
        log.info("getUserEntityByUserId(userId={})", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
    }

    @Override
    public User getUserEntityByUsername(String username) {
        log.info("getUserEntityByUsername(username={})", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User", username));
    }

    @Override
    public UserDTO getUserByUserId(String userId) {
        log.info("getUserByUserId(userId={})", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        return mapper.toUserDTO(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        log.info("getUserByUsername(username={})", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User", username));
        return mapper.toUserDTO(user);
    }

    @Override
    public Page<UserDTO> getUsersByRole(RoleName roleName, int page, int size) {
        log.info("getUsersByRole(roleName={})", roleName);
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByRolesRoleName(roleName, pageable)
                .map(mapper::toUserDTO);
    }
}
