package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.request.NotificationRequestParamDTO;
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
            @ModelAttribute NotificationRequestParamDTO params,
            Principal principal
    ) {
        Page<NotificationUserDTO> notifications = notificationReaderService
                .getNotificationsByUsername(principal.getName(), params);
        return ResponseEntity.ok().body(notifications);
    }

    @PatchMapping("/{notificationId}/me")
    @Operation(summary = "Update notification is read status", description = "Authenticated access")
    public ResponseEntity<ResponseDTO<NotificationUserDTO>> updateIsReadStatus(
            @PathVariable String notificationId,
            @RequestBody RequestNotificationUserDTO requestNotificationUserDTO,
            Principal principal
    ) {
        NotificationUserDTO notificationUserDTO = notificationUpdateService
                .updateNotificationIsReadStatus(principal.getName(), notificationId, requestNotificationUserDTO);
        ResponseDTO<NotificationUserDTO> responseDTO = ResponseDTO.<NotificationUserDTO>builder()
                .message("Notification updated successfully")
                .data(notificationUserDTO)
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }
}
