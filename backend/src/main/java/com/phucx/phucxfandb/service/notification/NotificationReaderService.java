package com.phucx.phucxfandb.service.notification;

import com.phucx.phucxfandb.dto.request.NotificationRequestParamDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface NotificationReaderService {
    Page<NotificationUserDTO> getNotifications(Authentication authentication, NotificationRequestParamDTO params);
}
