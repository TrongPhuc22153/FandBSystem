package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.NotificationRequestParamsDTO;
import com.phucx.phucxfandb.dto.request.RequestNotificationUserDTO;
import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.service.notification.NotificationReaderService;
import com.phucx.phucxfandb.service.notification.NotificationUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Notifications API", description = "Authentication endpoint for users")
@RequestMapping(value = "/api/v1/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationController {
    private final NotificationReaderService notificationReaderService;
    private final NotificationUpdateService notificationUpdateService;

    @GetMapping("/me")
    @Operation(summary = "Get notifications", description = "Authenticated access")
    public ResponseEntity<Page<NotificationUserDTO>> getNotifications(
            @ModelAttribute NotificationRequestParamsDTO params,
            Authentication authentication
    ) {
        Page<NotificationUserDTO> notifications = notificationReaderService
                .getNotifications(authentication, params);
        return ResponseEntity.ok().body(notifications);
    }

    @PatchMapping("/{id}/me")
    @Operation(summary = "Update notification as read status", description = "Authenticated access")
    public ResponseEntity<ResponseDTO<NotificationUserDTO>> updateIsReadStatus(
            @PathVariable String id,
            @RequestBody RequestNotificationUserDTO requestNotificationUserDTO,
            Principal principal
    ) {
        NotificationUserDTO notificationUserDTO = notificationUpdateService
                .updateNotificationIsReadStatus(principal.getName(), id, requestNotificationUserDTO);
        ResponseDTO<NotificationUserDTO> responseDTO = ResponseDTO.<NotificationUserDTO>builder()
                .message("Notification updated successfully")
                .data(notificationUserDTO)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
