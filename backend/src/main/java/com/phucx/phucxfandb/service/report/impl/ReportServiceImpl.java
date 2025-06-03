package com.phucx.phucxfandb.service.report.impl;

import com.phucx.phucxfandb.dto.response.AnalyticDTO;
import com.phucx.phucxfandb.dto.response.MetricDTO;
import com.phucx.phucxfandb.dto.response.ReportDTO;
import com.phucx.phucxfandb.entity.TableEntity;
import com.phucx.phucxfandb.entity.TableOccupancy;
import com.phucx.phucxfandb.enums.PaymentStatus;
import com.phucx.phucxfandb.repository.*;
import com.phucx.phucxfandb.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final TableOccupancyRepository tableOccupancyRepository;
    private final TableRepository tableRepository;

    @Override
    @Transactional(readOnly = true)
    public ReportDTO getReport(LocalDate startDate, LocalDate endDateTime) {
        long totalOrders = countOrdersOnDate(startDate.atStartOfDay(), endDateTime.atStartOfDay().plusDays(1));
        long totalReservations = countReservationsOnDate(startDate, endDateTime);
        long totalRevenue = sumPaymentsOnDate(startDate.atStartOfDay(), endDateTime.atStartOfDay().plusDays(1));
        long totalOccupiedTables = countOccupiedTables(LocalDate.now());

        return ReportDTO.builder()
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .totalReservations(totalReservations)
                .totalOccupiedTables(totalOccupiedTables)
                .build();
    }

    private Long countOccupiedTables(LocalDate date) {
        List<TableEntity> allTables = tableRepository.findAll();
        List<String> tableIds = allTables.stream()
                .map(TableEntity::getTableId)
                .collect(Collectors.toList());

        List<TableOccupancy> activeOccupancies = tableOccupancyRepository
                .findActiveOccupancies(date, tableIds);
        return (long) activeOccupancies.size();
    }

    private Long countOrdersOnDate(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return orderRepository.countTotalOrders(startDateTime, endDateTime);
    }

    private Long countReservationsOnDate(LocalDate startDate, LocalDate endDate) {
        return reservationRepository.countReservations(startDate, endDate);
    }

    private Long sumPaymentsOnDate(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return paymentRepository.sumTotalPaymentByStatus(PaymentStatus.SUCCESSFUL, startDateTime, endDateTime);
    }

    @Override
    @Transactional(readOnly = true)
    public AnalyticDTO getMetrics(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1);

        MetricDTO revenueMetric = getRevenue(startDateTime, endDateTime);
        MetricDTO categoryMetrics = getCategoryMetrics(startDateTime, endDateTime);
        MetricDTO topItemsMetric = getTopItems(startDateTime, endDateTime);

        return AnalyticDTO.builder()
                .revenue(revenueMetric)
                .categories(categoryMetrics)
                .topItems(topItemsMetric)
                .build();
    }

    public MetricDTO getCategoryMetrics(LocalDateTime startDateTime, LocalDateTime endDateTime) {
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

        return MetricDTO.builder()
                .labels(labels)
                .values(values)
                .build();
    }

    private MetricDTO getRevenue(LocalDateTime startDateTime, LocalDateTime endDateTime) {
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

        return MetricDTO.builder()
                .labels(labels)
                .values(data)
                .build();
    }

    private MetricDTO getTopItems(LocalDateTime start, LocalDateTime end) {
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

        return MetricDTO.builder()
                .labels(labels)
                .values(values)
                .build();
    }
}
