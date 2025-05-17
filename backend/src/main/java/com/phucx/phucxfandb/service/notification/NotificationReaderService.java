package com.phucx.phucxfandb.service.notification;

import com.phucx.phucxfandb.dto.request.NotificationRequestParamDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
import org.springframework.data.domain.Page;

public interface NotificationReaderService {
    Page<NotificationUserDTO> getNotificationsByUsername(String username, NotificationRequestParamDTO params);
}
