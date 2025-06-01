package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.phucx.phucxfandb.enums.OrderStatus;
import com.phucx.phucxfandb.enums.OrderType;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    String orderId;
    List<OrderDetailDTO> orderDetails;
    OrderStatus status;
    BigDecimal totalPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime orderDate;
    CustomerDTO customer;
    EmployeeDTO employee;
    TableOccupancyDTO tableOccupancy;
    PaymentDTO payment;
    ShippingAddressDTO shippingAddress;
    OrderType type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime lastModifiedAt;
}
