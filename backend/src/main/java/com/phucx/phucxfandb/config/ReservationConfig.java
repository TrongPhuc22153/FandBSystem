package com.phucx.phucxfandb.config;

import com.phucx.phucxfandb.dto.event.ReservationEvent;
import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.enums.ReservationStatus;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import com.phucx.phucxfandb.service.reservation.ReservationUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationConfig {
    @Value("${reservation.cutoff.minutes}")
    private long cutoffMinutes;
    private final ConcurrentSkipListMap<LocalTime, List<Reservation>> reservationQueue;
    private final ReservationReaderService reservationReaderService;
    private final ReservationUpdateService reservationUpdateService;
    private final AtomicReference<LocalDateTime> lastUpdate = new AtomicReference<>(null);

    public void retrieveTasksForToday() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        if (hasRetrievedToday(now)) {
            return;
        }

        log.info("Retrieving reservations for today: {}", today);

        List<Reservation> tasks = reservationReaderService.getOverDueReservations(now);

        Map<LocalTime, List<Reservation>> groupedTasks = tasks.stream()
                .collect(Collectors.groupingBy(Reservation::getStartTime));
        reservationQueue.putAll(groupedTasks);

        log.info("Loaded {} reservations for today", tasks.size());
        lastUpdate.set(now);
    }

    private boolean hasRetrievedToday(LocalDateTime now) {
        LocalDateTime last = lastUpdate.get();
        return last != null &&
                now.toLocalDate().equals(last.toLocalDate()) &&
                now.minusMinutes(10).isBefore(last);
    }

    @Scheduled(fixedRateString = "${reservation.process.interval}")
    public void processTasks() {
        retrieveTasksForToday();
        if (reservationQueue.isEmpty()) {
            return;
        }

        LocalTime currentTime = LocalTime.now();
        NavigableMap<LocalTime, List<Reservation>> dueTasks = reservationQueue
                .headMap(currentTime, true);

        dueTasks.forEach((startTime, reservations) -> {
            log.info("Processing {} reservation for {}", reservations.size(), startTime);
            reservations.forEach(this::processReservation);
            reservationQueue.remove(startTime);
        });
    }

    private boolean isOverdueReservation(Reservation reservation){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(reservation.getDate(), reservation.getStartTime());
        LocalDateTime cutoffDateTime = reservationDateTime.plus(cutoffMinutes, ChronoUnit.MINUTES);

        return (now.isAfter(cutoffDateTime) &&
                reservation.getTableOccupancy() == null &&
                (reservation.getStatus() == ReservationStatus.PENDING ||
                        reservation.getStatus() == ReservationStatus.CONFIRMED ||
                        reservation.getStatus() == ReservationStatus.PREPARED ||
                        reservation.getStatus() == ReservationStatus.PREPARING));

    }

    private void processReservation(Reservation reservation) {
        int maxRetries = 3;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                Reservation currentReservation = reservationReaderService
                        .getReservationEntity(reservation.getReservationId());
                // Check if reservation is overdue, not seated, and in PENDING or CONFIRMED status
                if (isOverdueReservation(currentReservation)) {
                    log.info("Marking reservation {} as NO_SHOW", currentReservation.getReservationId());
                    reservationUpdateService.updateReservationStatus(currentReservation.getReservationId(), ReservationStatus.CANCELED);
                }
                return;
            } catch (Exception e) {
                log.warn("Attempt {}/{} failed for reservation {}: {}", attempt, maxRetries, reservation.getReservationId(), e.getMessage());
                if (attempt == maxRetries) {
                    log.error("Max retries reached for reservation {}", reservation.getReservationId());
                }
            }
        }
    }

    @Async
    @EventListener
    public void handleReservationUpdate(ReservationEvent event) {
        switch (event.getAction()) {
            case DELETE -> {
                reservationQueue.forEach((time, reservations) ->
                        reservations.removeIf(reservation -> reservation.getReservationId().equals(event.getId())));
                log.info("Removed reservation {}", event.getId());
            }
            default -> log.warn("Unsupported event action: {}", event.getAction());
        }
    }
}
