package com.phucx.phucxfandb.config;

import com.phucx.phucxfandb.dto.event.ReservationEvent;
import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.enums.ReservationStatus;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import com.phucx.phucxfandb.service.reservation.ReservationUpdateService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationConfig {
    private final ConcurrentSkipListMap<LocalTime, List<Reservation>> reservationQueue;
    private final ReservationReaderService reservationReaderService;
    private final ReservationUpdateService reservationUpdateService;

    private LocalDate lastUpdate = null;

    @PostConstruct
    public void onApplicationStart() {
        log.info("Application started, processing overdue reservations...");
        processTasks(); // Run immediately on startup
    }

    public void retrieveTasksForToday() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        if (hasRetrievedToday(today)) {
            return;
        }

        log.info("Retrieving reservations for today: {}", today);

        List<Reservation> tasks = reservationReaderService.getReservations(now);

        Map<LocalTime, List<Reservation>> groupedTasks = tasks.stream()
                .collect(Collectors.groupingBy(Reservation::getStartTime));
        reservationQueue.putAll(groupedTasks);

        log.info("Loaded {} reservations for today", tasks.size());

        lastUpdate = today;
    }

    @Scheduled(fixedRate = 10000)
    public void processTasks() {
        retrieveTasksForToday();
        if (reservationQueue.isEmpty()) {
            return;
        }

        LocalTime currentTime = LocalTime.now();
        NavigableMap<LocalTime, List<Reservation>> dueTasks = reservationQueue
                .headMap(currentTime, true);

        if (!dueTasks.isEmpty()) {
            dueTasks.forEach((startTime, reservations) -> {
                log.info("Processing {} reservation for {}", reservations.size(), startTime);
                reservations.forEach(this::processReservation);
                reservationQueue.remove(startTime);
            });
            log.info("Completed processing. Remaining reservations: {}", reservationQueue.size());
        }
    }

    private boolean hasRetrievedToday(LocalDate today) {
        return lastUpdate != null && !today.isAfter(lastUpdate);
    }

    /*
     * No repetition, delivery is not guaranteed.
     */
    private void processReservation(Reservation reservation) {
        try {
            Reservation currentReservation = reservationReaderService
                    .getReservationEntity(reservation.getReservationId());

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime reservationDateTime = LocalDateTime.of(currentReservation.getDate(), currentReservation.getStartTime());
            LocalDateTime cutoffDateTime = reservationDateTime.plus(30, ChronoUnit.MINUTES);

            // Check if reservation is overdue, not seated, and in PENDING or CONFIRMED status
            if (now.isAfter(cutoffDateTime) &&
                    currentReservation.getTableOccupancy() == null &&
                    (currentReservation.getStatus() == ReservationStatus.PENDING ||
                            currentReservation.getStatus() == ReservationStatus.CONFIRMED ||
                            currentReservation.getStatus() == ReservationStatus.PREPARED ||
                            currentReservation.getStatus() == ReservationStatus.PREPARING)) {

                log.info("Marking reservation {} as NO_SHOW", currentReservation.getReservationId());
                reservationUpdateService.updateReservationStatus(currentReservation.getReservationId(), ReservationStatus.CANCELED);
            }
        } catch (Exception e) {
            log.error("Failed to process reservation {}: {}", reservation.getReservationId(), e.getMessage());
        }
    }

    @Async
    @EventListener
    public void handleReservationUpdate(ReservationEvent event) {
        reservationQueue.forEach((time, reservations) -> reservations.removeIf(reservation -> reservation.getReservationId().equals(event.getId())));
        switch (event.getAction()){
            case DELETE -> {
                log.info("Remove reservation {}", event.getId());
            }
            default -> log.warn("Missing event action");
        }
    }
}
