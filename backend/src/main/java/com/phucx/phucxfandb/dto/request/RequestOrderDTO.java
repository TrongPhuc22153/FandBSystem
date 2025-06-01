package com.phucx.phucxfandb.dto.request;

import com.phucx.phucxfandb.enums.OrderAction;
import com.phucx.phucxfandb.enums.OrderStatus;
import com.phucx.phucxfandb.enums.OrderType;
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

    @Size(min = 3, max = 36, message = "Customer ID must be between 3 and 36 characters")
    private String customerId;

    @Size(min = 3, max = 36, message = "Employee ID must be between 3 and 36 characters")
    private String employeeId;

    @Size(min = 3, max = 36, message = "Table occupancy ID must be between 3 and 36 characters")
    private String tableOccupancyId;

    @NotNull(message = "Order type cannot be null")
    private OrderType type;

    private Long shippingAddressId;

    private OrderStatus status;

    private OrderAction action;

    @NotEmpty(message = "Order must have at least one order detail")
    private List<@Valid RequestOrderDetailsDTO> orderDetails;
}
