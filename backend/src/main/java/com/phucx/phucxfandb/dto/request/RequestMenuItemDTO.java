package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.ValidationGroups;
import com.phucx.phucxfandb.enums.MenuItemStatus;
import com.phucx.phucxfandb.enums.OrderItemStatus;
import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestMenuItemDTO {
    private String id;

    @NotNull(message = "Product ID cannot be null")
    @Positive(message = "Product ID must be positive")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @Size(max = 255, message = "Special instruction cannot exceed 255 character")
    private String specialInstruction;

    @NotNull(message = "Reservation item status cannot be null", groups = ValidationGroups.UpdateReservationItemStatus.class)
    private MenuItemStatus status;
}