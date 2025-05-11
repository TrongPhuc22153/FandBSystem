package com.phucx.phucxfandb.service.notification.imps;

import com.phucx.phucxfandb.dto.request.NotificationRequestParamDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
import com.phucx.phucxfandb.mapper.NotificationUserMapper;
import com.phucx.phucxfandb.repository.NotificationUserRepository;
import com.phucx.phucxfandb.service.notification.NotificationReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationReaderServiceImpl implements NotificationReaderService {
    private final NotificationUserRepository notificationUserRepository;
    private final NotificationUserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationUserDTO> getOrderNotification(String orderID, String username) {
        log.info("getOrderNotification(orderId={}, username={})", orderID, username);
        return notificationUserRepository.findByNotificationOrderOrderIdAndReceiverUsername(orderID, username)
                .stream().map(mapper::toNotificationUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationUserDTO> getNotificationsByUsername(String username, NotificationRequestParamDTO params) {
        log.info("getNotificationsByUsername(username={}, params={})", username, params);
        Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), Sort.by(params.getDirection(), params.getField()));
        return notificationUserRepository.findByReceiverUsername(username, pageable)
                .map(mapper::toNotificationUserDTO);
    }
}
