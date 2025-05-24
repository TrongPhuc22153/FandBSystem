package com.phucx.phucxfandb.service.notification;

import com.phucx.phucxfandb.dto.request.NotificationRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface NotificationReaderService {
    Page<NotificationUserDTO> getNotifications(Authentication authentication, NotificationRequestParamsDTO params);
}
