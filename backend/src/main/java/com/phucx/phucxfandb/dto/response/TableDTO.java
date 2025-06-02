package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TableDTO {
    String tableId;
    Integer tableNumber;
    String location;
    Integer capacity;
    String occupancyId;
    String status;
    String reservationId;
    String contactName;
    String phone;
    Integer partySize;
    String notes;
    LocalTime startAt;
    LocalTime occupiedAt;
    Boolean isDeleted;
    String lastModifiedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime lastModifiedAt;
}
