package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.ValidationGroups;
import com.phucx.phucxfandb.enums.OrderItemStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrderDetailsDTO {

    @Size(max = 36, message = "Order detail Id cannot exceed 36 character")
    private String id;

    @NotNull(message = "Product Id cannot be null")
    @Positive(message = "Product Id must be a positive number")
    private Long productId;

    @Min(1)
    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be a positive number")
    private Integer quantity;

    @Size(max = 255, message = "Special instruction cannot exceed 255 character")
    private String specialInstruction;

    @NotNull(message = "Order item status cannot be null", groups = ValidationGroups.UpdateOrderItemStatus.class)
    private OrderItemStatus status;
}
