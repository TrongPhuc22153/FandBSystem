package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.phucx.phucxfandb.enums.ReservationStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationDTO {
    String reservationId;
    Integer numberOfGuests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    LocalTime endTime;
    String notes;
    BigDecimal totalPrice;
    ReservationStatus status;
    CustomerDTO customer;
    EmployeeDTO employee;
    PaymentDTO payment;
    ReservationTableDTO table;
    List<MenuItemDTO> menuItems;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime lastModifiedAt;
}
