package com.phucx.phucxfandb.dto.request;

import com.fasterxml.jackson.annotation.JsonView;
import com.phucx.phucxfandb.constant.ValidationGroups;
import com.phucx.phucxfandb.constant.Views;
import com.phucx.phucxfandb.enums.WaitListStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonView(Views.Default.class)
public class RequestWaitListDTO {
    @NotBlank(message = "Contact name is required")
    @Size(max = 30, message = "Contact name must not exceed 30 characters")
    private String contactName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9\\-+]{9,15}$", message = "Invalid phone number format")
    private String phone;

    @NotNull(message = "Party size is required")
    @Min(value = 1, message = "Party size must be at least 1")
    private Integer partySize;

    @Size(max = 36, message = "Table ID must not exceed 36 characters", groups =
            ValidationGroups.UpdateWaitListStatus.class)
    @JsonView(Views.UpdateWaitListStatus.class)
    private String tableId;

    @NotNull(message = "Status is required", groups =
            ValidationGroups.UpdateWaitListStatus.class)
    @JsonView(Views.UpdateWaitListStatus.class)
    private WaitListStatus status;

    @Size(max = 255, message = "Notes must not exceed 255 characters")
    private String notes;
}
