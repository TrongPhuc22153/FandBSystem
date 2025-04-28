package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.NotificationTopic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestNotificationDTO {
    @NotBlank(message = "Sender ID cannot be empty")
    @Size(min = 3, max = 20, message = "Sender username must be between 3 and 20 characters")
    private String senderUsername;

    @NotBlank(message = "Receiver ID cannot be empty")
    @Size(min = 3, max = 20, message = "Receiver username must be between 3 and 20 characters")
    private String receiverUsername;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Message cannot be empty")
    @Size(min = 10, max = 1000, message = "Message must be between 10 and 1000 characters")
    private String message;

    @Size(max = 255, message = "Picture URL must not exceed 255 characters")
    private String picture;

    @NotNull(message = "Topic can not be null")
    private NotificationTopic topic;

    @Size(max = 36, message = "Replied-to ID must not exceed 36 characters")
    private String repliedTo;
}
