package com.phucx.phucxfandb.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestTopicDTO {

    private Long topicId;

    @NotBlank(message = "Topic name cannot be blank")
    @Size(min = 2, max = 20, message = "Topic name must be between 2 and 20 characters")
    private String topicName;
}