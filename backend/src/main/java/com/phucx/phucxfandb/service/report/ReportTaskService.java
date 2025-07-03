package com.phucx.phucxfandb.service.report;

import com.phucx.phucxfandb.dto.response.MetricDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public interface ReportTaskService {
    CompletableFuture<BigDecimal> calculateAverageOrderValue(LocalDateTime startDateTime, LocalDateTime endDateTime);

    CompletableFuture<Long> countOrdersOnDate(LocalDateTime startDateTime, LocalDateTime endDateTime);

    CompletableFuture<Long> countReservationsOnDate(LocalDate startDate, LocalDate endDate);

    CompletableFuture<Long> sumPaymentsOnDate(LocalDateTime startDateTime, LocalDateTime endDateTime);

    CompletableFuture<MetricDTO> getRevenue(LocalDateTime startDateTime, LocalDateTime endDateTime);

    CompletableFuture<MetricDTO> getTopItems(LocalDateTime start, LocalDateTime end);

    CompletableFuture<MetricDTO> getCategoryMetrics(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
