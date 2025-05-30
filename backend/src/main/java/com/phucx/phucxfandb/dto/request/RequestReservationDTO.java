package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.enums.ReservationAction;
import com.phucx.phucxfandb.enums.ReservationStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestReservationDTO {

    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "Number of guests must be at least 1")
    private Integer numberOfGuests;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @NotEmpty(message = "Menu can not be null")
    private List<@Valid RequestMenuItemDTO> menuItems;

    private String notes;

    @Size(min = 3, max = 36, message = "Customer ID must be between 3 and 36 characters")
    private String customerId;

    @Size(min = 3, max = 36, message = "Employee ID must be between 3 and 36 characters")
    private String employeeId;

    @Size(min = 3, max = 36, message = "Table ID must be between 3 and 36 characters")
    private String tableId;

    private ReservationAction action;

    private ReservationStatus status;

}
