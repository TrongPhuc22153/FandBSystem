package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.phucx.phucxfandb.enums.WaitListStatus;
import lombok.*;

import java.time.LocalDateTime;


@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WaitListDTO {
    String id;

    String contactName;

    String phone;

    Integer partySize;

    WaitListStatus status;

    String notes;

    OrderDTO order;

    ReservationTableDTO table;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime lastModifiedAt;
}
