package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.ReservationStatus;
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
    private String reservationId;

    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "Number of guests must be at least 1")
    private Integer numberOfGuests;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    private String notes;

    private ReservationStatus status;

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotBlank(message = "Table ID is required")
    private String tableId;

    @NotEmpty(message = "Menu can not be null")
    private List<@Valid RequestMenuItemDTO> menuItems;

}
