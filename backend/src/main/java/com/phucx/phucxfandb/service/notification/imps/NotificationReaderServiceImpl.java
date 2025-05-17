package com.phucx.phucxfandb.service.notification.imps;

import com.phucx.phucxfandb.dto.request.NotificationRequestParamDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
import com.phucx.phucxfandb.entity.NotificationUser;
import com.phucx.phucxfandb.mapper.NotificationUserMapper;
import com.phucx.phucxfandb.repository.NotificationUserRepository;
import com.phucx.phucxfandb.service.notification.NotificationReaderService;
import com.phucx.phucxfandb.specifications.NotificationUserSpecification;
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
public class NotificationReaderServiceImpl implements NotificationReaderService {
    private final NotificationUserRepository notificationUserRepository;
    private final NotificationUserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationUserDTO> getNotificationsByUsername(String username, NotificationRequestParamDTO params) {
        Pageable pageable = PageRequest.of(
                params.getPage(),
                params.getSize(),
                Sort.by(params.getDirection(), params.getField())
        );
        Specification<NotificationUser> spec = Specification
                .where(NotificationUserSpecification.hasReceiverUsername(username))
                .and(NotificationUserSpecification.isRead(params.getIsRead()));

        return notificationUserRepository.findAll(spec, pageable)
                .map(mapper::toNotificationUserDTO);
    }
}
