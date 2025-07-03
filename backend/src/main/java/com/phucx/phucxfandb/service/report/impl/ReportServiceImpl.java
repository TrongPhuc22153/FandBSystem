package com.phucx.phucxfandb.service.report.impl;

import com.phucx.phucxfandb.dto.response.AnalyticDTO;
import com.phucx.phucxfandb.dto.response.MetricDTO;
import com.phucx.phucxfandb.dto.response.ReportDTO;
import com.phucx.phucxfandb.service.report.ReportService;
import com.phucx.phucxfandb.service.report.ReportTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportTaskService reportTaskService;

    @Override
    @Transactional(readOnly = true)
    public ReportDTO getReport(LocalDate startDate, LocalDate endDate){
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1);

        CompletableFuture<Long> countOrdersOnDateFuture = reportTaskService.countOrdersOnDate(startDateTime, endDateTime);
        CompletableFuture<Long> totalReservationsFuture = reportTaskService.countReservationsOnDate(startDate, endDate);
        CompletableFuture<Long> totalRevenueFuture = reportTaskService.sumPaymentsOnDate(startDateTime, endDateTime);
        CompletableFuture<BigDecimal> averageOrderValueFuture = reportTaskService.calculateAverageOrderValue(startDateTime, endDateTime);

        CompletableFuture.allOf(
                countOrdersOnDateFuture,
                totalReservationsFuture,
                totalRevenueFuture,
                averageOrderValueFuture
        ).join();

        long totalOrders = countOrdersOnDateFuture.join();
        long totalReservations = totalReservationsFuture.join();
        long totalRevenue = totalRevenueFuture.join();
        BigDecimal averageOrderValue = averageOrderValueFuture.join();

        return ReportDTO.builder()
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .totalReservations(totalReservations)
                .averageOrderValue(averageOrderValue)
                .build();
    }



    @Override
    @Transactional(readOnly = true)
    public AnalyticDTO getMetrics(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1);

        CompletableFuture<MetricDTO> revenueMetricFuture = reportTaskService.getRevenue(startDateTime, endDateTime);
        CompletableFuture<MetricDTO> categoryMetricsFuture = reportTaskService.getCategoryMetrics(startDateTime, endDateTime);
        CompletableFuture<MetricDTO> topItemsMetricFuture = reportTaskService.getTopItems(startDateTime, endDateTime);

        CompletableFuture.allOf(
                revenueMetricFuture,
                categoryMetricsFuture,
                topItemsMetricFuture
        ).join();

        MetricDTO revenueMetric = revenueMetricFuture.join();
        MetricDTO categoryMetrics = categoryMetricsFuture.join();
        MetricDTO topItemsMetric = topItemsMetricFuture.join();

        return AnalyticDTO.builder()
                .revenue(revenueMetric)
                .categories(categoryMetrics)
                .topItems(topItemsMetric)
                .build();
    }
}
