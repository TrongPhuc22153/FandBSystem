package com.phucx.phucxfoodshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.phucxfoodshop.constant.NotificationBroadCast;
import com.phucx.phucxfoodshop.constant.WebConstant;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.model.NotificationDetail;
import com.phucx.phucxfoodshop.model.NotificationSummary;
import com.phucx.phucxfoodshop.model.ResponseFormat;
import com.phucx.phucxfoodshop.service.notification.MarkUserNotificationService;
import com.phucx.phucxfoodshop.service.notification.NotificationService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/notification/employee", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeNotificationController {
    private final NotificationService notificationService;
    private final MarkUserNotificationService markUserNotificationService;

    @Operation(summary = "Mark notification as read", tags = {"employee", "notification", "post"},
        description = "Update notification status")
    @PostMapping("/notification/mark")
    public ResponseEntity<ResponseFormat> markAsRead(
        @RequestParam(name = "type", required = true) String marktype,
        @RequestBody NotificationDetail notification, 
        Authentication authentication
    ) throws NotFoundException{
        Boolean status = markUserNotificationService.markAsReadForEmployeeByUsername(
            notification.getNotificationID(), authentication.getName(), marktype);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    // GET NOTIFICATIONS
    @Operation(summary = "Get notifications", tags = {"employee", "notification", "get"})
    @GetMapping("/notification")
    public ResponseEntity<Page<NotificationDetail>> getNotifications(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        Authentication authentication
    ){
        pageNumber=pageNumber!=null?pageNumber:0;
        Page<NotificationDetail> notifications = notificationService
            .getNotificationsByUsernameOrBroadCast(
                authentication.getName(), 
                NotificationBroadCast.ALL_EMPLOYEES, 
                pageNumber, WebConstant.NOTIFICATION_PAGE_SIZE);
        return ResponseEntity.ok().body(notifications);
    }

    @Operation(summary = "Get notification summary", tags = {"employee", "notification", "get"},
        description = "Get number of unread notifications")
    @GetMapping("/summary")
    public ResponseEntity<NotificationSummary> getNotificationSummary(Authentication authentication){
        NotificationSummary notificationSummary = new NotificationSummary();
        Long totalOfUnreadNotifications = notificationService
            .getUserTotalNumberOfUnreadNotificationsByUsername(
            authentication.getName(), NotificationBroadCast.ALL_EMPLOYEES);
        notificationSummary.setTotalOfUnreadNotifications(totalOfUnreadNotifications);
        return ResponseEntity.ok().body(notificationSummary);
    }
}
