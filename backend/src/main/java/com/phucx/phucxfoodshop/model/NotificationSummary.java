package com.phucx.phucxfoodshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSummary {
    private Long totalOfUnreadNotifications;
}
