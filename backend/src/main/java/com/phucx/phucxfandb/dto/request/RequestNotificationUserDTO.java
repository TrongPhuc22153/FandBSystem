package com.phucx.phucxfandb.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestNotificationUserDTO {
    private String id;

    @Valid
    @NotNull(message = "Notification cannot be null")
    private RequestNotificationDTO notification;

    @NotBlank(message = "Sender cannot be blank")
    private String sender;

    @NotBlank(message = "Receiver cannot be blank")
    private String receiver;

    private Boolean isRead = false;
}
