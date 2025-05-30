package com.phucx.phucxfandb.service.notification.impl;

import com.phucx.phucxfandb.enums.RoleName;
import com.phucx.phucxfandb.dto.request.NotificationRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
import com.phucx.phucxfandb.entity.NotificationUser;
import com.phucx.phucxfandb.mapper.NotificationUserMapper;
import com.phucx.phucxfandb.repository.NotificationUserRepository;
import com.phucx.phucxfandb.service.notification.NotificationReaderService;
import com.phucx.phucxfandb.specifications.NotificationUserSpecification;
import com.phucx.phucxfandb.utils.RoleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationReaderServiceImpl implements NotificationReaderService {
    private final NotificationUserRepository notificationUserRepository;
    private final NotificationUserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationUserDTO> getNotifications(Authentication authentication, NotificationRequestParamsDTO params) {
        String username = authentication.getName();
        Pageable pageable = PageRequest.of(
                params.getPage(),
                params.getSize(),
                Sort.by(params.getDirection(), params.getField())
        );

        List<RoleName> roleNames = RoleUtils.getRoles(authentication.getAuthorities());

        Specification<NotificationUser> finalSpec = getNotificationUserSpecification(roleNames, username);

        Specification<NotificationUser> spec = Specification
                .where(NotificationUserSpecification.isRead(params.getIsRead()))
                .and(finalSpec);

        return notificationUserRepository.findAll(spec, pageable)
                .map(mapper::toNotificationUserDTO);
    }

    private static Specification<NotificationUser> getNotificationUserSpecification(List<RoleName> roleNames, String username) {
        Specification<NotificationUser> roleSpec = null;
        for (RoleName roleName : roleNames) {
            if (roleName == RoleName.ADMIN || roleName == RoleName.EMPLOYEE || roleName == RoleName.CUSTOMER) {
                Specification<NotificationUser> singleRoleSpec = NotificationUserSpecification.hasGroupOrUsername(username, roleName);
                roleSpec = roleSpec == null ? singleRoleSpec : roleSpec.or(singleRoleSpec);
            }
        }

        return roleSpec != null ? roleSpec : NotificationUserSpecification.hasReceiverUsername(username);
    }
}
