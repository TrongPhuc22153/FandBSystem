package com.phucx.phucxfandb.controller;

import com.phucx.phucxfandb.dto.response.NotificationUserDTO;
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

//    @Operation(summary = "Mark notification as read", description = "Customer access")
//    @PostMapping(value = "/notification/{notificationId}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ResponseDTO<Boolean>> markAsReadNotification(
//            @PathVariable String notificationId,
//            Principal principal
//    ){
//        boolean status = notificationUpdateService.(
//                notification.getNotificationID(), principal.getName(), marktype);
//        ResponseDTO<Boolean> responseDTO = ResponseDTO.<Boolean>builder()
//                .message("Notification marked as read successfully")
//                .data(status)
//                .build();
//        return ResponseEntity.ok().body(responseDTO);
//    }

    @GetMapping("/me")
    @Operation(summary = "Get notifications", description = "Authenticated access")
    public ResponseEntity<Page<NotificationUserDTO>> getNotifications(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", defaultValue = "10") Integer pageSize,
            Principal principal
    ) {
        Page<NotificationUserDTO> notifications = notificationReaderService
                .getNotificationsByUsername(principal.getName(), pageNumber, pageSize);
        return ResponseEntity.ok().body(notifications);
    }
}
