package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.phucx.phucxfandb.enums.TableOccupancyStatus;
import lombok.*;

import java.time.LocalDateTime;


@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TableOccupancyDTO {
    String id;

    String contactName;

    String phone;

    Integer partySize;

    TableOccupancyStatus status;

    String notes;

    OrderDTO order;

    ReservationDTO reservation;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime occupancyStartTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime occupancyEndTime;

    TableDTO table;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime lastModifiedAt;
}
