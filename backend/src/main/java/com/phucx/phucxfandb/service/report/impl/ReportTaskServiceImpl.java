package com.phucx.phucxfandb.service.report.impl;

import com.phucx.phucxfandb.dto.response.MetricDTO;
import com.phucx.phucxfandb.enums.PaymentStatus;
import com.phucx.phucxfandb.repository.OrderRepository;
import com.phucx.phucxfandb.repository.PaymentRepository;
import com.phucx.phucxfandb.repository.ProductRepository;
import com.phucx.phucxfandb.repository.ReservationRepository;
import com.phucx.phucxfandb.service.report.ReportTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ReportTaskServiceImpl implements ReportTaskService {
    private final OrderRepository orderRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;

    @Async
    @Override
    public CompletableFuture<BigDecimal> calculateAverageOrderValue(LocalDateTime startDateTime, LocalDateTime endDateTime){
        BigDecimal averageValue = orderRepository.calculateAverageOrderValueWithPaymentStatus(startDateTime, endDateTime, PaymentStatus.SUCCESSFUL);
        return CompletableFuture.completedFuture(averageValue);
    }

    @Async
    @Override
    public CompletableFuture<Long> countOrdersOnDate(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return CompletableFuture.completedFuture(orderRepository.countTotalOrders(startDateTime, endDateTime));
    }

    @Async
    @Override
    public CompletableFuture<Long> countReservationsOnDate(LocalDate startDate, LocalDate endDate) {
        return CompletableFuture.completedFuture(reservationRepository.countReservations(startDate, endDate));
    }

    @Async
    @Override
    public CompletableFuture<Long> sumPaymentsOnDate(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return CompletableFuture.completedFuture(paymentRepository.sumTotalPaymentByStatus(PaymentStatus.SUCCESSFUL, startDateTime, endDateTime));
    }

    @Async
    @Override
    public CompletableFuture<MetricDTO> getRevenue(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Object[]> results = paymentRepository.getDailyPaymentsThisWeek(startDateTime, endDateTime, PaymentStatus.SUCCESSFUL);

        Map<DayOfWeek, Long> revenueMap = new EnumMap<>(DayOfWeek.class);
        for (Object[] row : results) {
            LocalDate date = ((Date) row[0]).toLocalDate();
            BigDecimal amount = (BigDecimal) row[1];
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            revenueMap.put(dayOfWeek, revenueMap.getOrDefault(dayOfWeek, 0L) + amount.longValue());
        }

        List<String> labels = Arrays.stream(DayOfWeek.values())
                .map(d -> d.name().substring(0, 1).toUpperCase() + d.name().substring(1).toLowerCase().substring(1, 3))
                .map(s -> switch (s) {
                    case "Mo" -> "Mon";
                    case "Tu" -> "Tue";
                    case "We" -> "Wed";
                    case "Th" -> "Thu";
                    case "Fr" -> "Fri";
                    case "Sa" -> "Sat";
                    case "Su" -> "Sun";
                    default -> s;
                })
                .toList();

        List<Long> data = Arrays.stream(DayOfWeek.values())
                .map(day -> revenueMap.getOrDefault(day, 0L))
                .toList();

        return CompletableFuture.completedFuture(MetricDTO.builder()
                .labels(labels)
                .values(data)
                .build());
    }

    @Async
    @Override
    public CompletableFuture<MetricDTO> getTopItems(LocalDateTime start, LocalDateTime end) {
        Map<String, Long> productSales = new HashMap<>();

        List<Object[]> orderResults = productRepository.getProductSalesFromOrders(start, end);
        for (Object[] row : orderResults) {
            String productName = (String) row[0];
            Long quantity = ((Number) row[1]).longValue();
            productSales.merge(productName, quantity, Long::sum);
        }

        List<Object[]> menuItemResults = productRepository.getProductSalesFromReservations(start.toLocalDate(), end.toLocalDate());
        for (Object[] row : menuItemResults) {
            String productName = (String) row[0];
            Long quantity = ((Number) row[1]).longValue();
            productSales.merge(productName, quantity, Long::sum);
        }

        List<Map.Entry<String, Long>> sorted = productSales.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(4)
                .toList();

        List<String> labels = new ArrayList<>();
        List<Long> values = new ArrayList<>();
        for (Map.Entry<String, Long> entry : sorted) {
            labels.add(entry.getKey());
            values.add(entry.getValue());
        }

        return CompletableFuture.completedFuture(MetricDTO.builder()
                .labels(labels)
                .values(values)
                .build());
    }

    @Async
    @Override
    public CompletableFuture<MetricDTO> getCategoryMetrics(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Map<String, Long> categorySales = new HashMap<>();

        List<Object[]> fromOrders = productRepository.getCategorySalesFromOrders(startDateTime, endDateTime);
        for (Object[] row : fromOrders) {
            String category = (String) row[0];
            Long quantity = ((Number) row[1]).longValue();
            categorySales.merge(category, quantity, Long::sum);
        }

        List<Object[]> fromReservations = productRepository.getCategorySalesFromReservations(startDateTime.toLocalDate(), endDateTime.toLocalDate());
        for (Object[] row : fromReservations) {
            String category = (String) row[0];
            Long quantity = ((Number) row[1]).longValue();
            categorySales.merge(category, quantity, Long::sum);
        }

        List<String> labels = new ArrayList<>(categorySales.keySet());
        List<Long> values = labels.stream().map(categorySales::get).toList();

        return CompletableFuture.completedFuture(
                MetricDTO.builder()
                        .labels(labels)
                        .values(values)
                        .build()
        );
    }
}
