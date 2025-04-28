package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.phucx.phucxfandb.constant.OrderStatus;
import com.phucx.phucxfandb.constant.OrderType;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class OrderDTO {
    String orderId;
    List<OrderDetailDTO> orderDetails;
    OrderStatus status;
    BigDecimal totalPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime orderDate;
    ReservationTableDTO table;
    CustomerDTO customer;
    EmployeeDTO employee;
    OrderType type;
}
