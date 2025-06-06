package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.phucx.phucxfandb.enums.PaymentStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDTO {
    String paymentId;
    LocalDateTime paymentDate;
    String transactionID;
    BigDecimal amount;
    PaymentStatus status;
    CustomerDTO customer;
    EmployeeDTO employee;
    OrderDTO order;
    ReservationDTO reservation;
    String method;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime lastModifiedAt;
}
