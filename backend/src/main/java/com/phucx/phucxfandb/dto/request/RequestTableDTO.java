package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.ValidationGroups;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestTableDTO {
    private String tableId;

    @NotNull(message = "Table number cannot be null")
    @Positive(message = "Table number must be positive")
    private Integer tableNumber = 1;

    @Size(max = 100, message = "Location cannot exceed 100 characters")
    private String location;

    @Min(1)
    @NotNull(message = "Capacity cannot be null")
    @Positive(message = "Capacity must be positive")
    private Integer capacity;

    @NotNull(message = "Is deleted cannot be null", groups = ValidationGroups.UpdateTableStatus.class)
    private Boolean isDeleted;
}
