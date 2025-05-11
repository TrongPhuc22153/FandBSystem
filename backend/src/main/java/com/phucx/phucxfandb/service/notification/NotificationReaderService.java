package com.phucx.phucxfandb.service.notification;

import com.phucx.phucxfandb.dto.request.NotificationRequestParamDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationReaderService {
    List<NotificationUserDTO> getOrderNotification(String orderID, String username);
    Page<NotificationUserDTO> getNotificationsByUsername(String username, NotificationRequestParamDTO params);
}
