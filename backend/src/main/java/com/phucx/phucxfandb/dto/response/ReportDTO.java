package com.phucx.phucxfandb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportDTO {
    Long totalOrders;
    Long totalRevenue;
    Long totalReservations;
    BigDecimal averageOrderValue;
}
