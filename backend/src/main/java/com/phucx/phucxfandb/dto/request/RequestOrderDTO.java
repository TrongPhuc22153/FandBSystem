package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.constant.OrderStatus;
import com.phucx.phucxfandb.constant.OrderType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestOrderDTO {
    @Size(min = 3, max = 36, message = "Order ID must be between 3 and 36 characters")
    private String orderId;

    @NotBlank(message = "Customer ID cannot be empty")
    @Size(min = 3, max = 36, message = "Customer ID must be between 3 and 36 characters")
    private String customerId;

    @Size(min = 3, max = 36, message = "Employee ID must be between 3 and 36 characters")
    private String employeeId;

    @Size(min = 3, max = 36, message = "Table ID must be between 3 and 36 characters")
    private String tableId;

    @NotNull(message = "Order type cannot be null")
    private OrderType type;

    @NotNull(message = "Status cannot be null")
    private OrderStatus status;

    @Valid
    @NotNull(message = "Order details cannot be null")
    @Size(min = 1, message = "Order must have at least one order detail")
    private List<RequestOrderDetailsDTO> orderDetails;
}
