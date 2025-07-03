package com.phucx.phucxfandb.service.table.impl;

import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.entity.TableOccupancy;
import com.phucx.phucxfandb.repository.ReservationRepository;
import com.phucx.phucxfandb.repository.TableOccupancyRepository;
import com.phucx.phucxfandb.service.table.TableTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TableTaskServiceImpl implements TableTaskService {
    private final TableOccupancyRepository tableOccupancyRepository;
    private final ReservationRepository reservationRepository;

    @Async
    @Override
    public CompletableFuture<List<TableOccupancy>> getActiveOccupancies(LocalDate date, LocalTime time, List<String> tableIds) {
        return CompletableFuture.completedFuture(tableOccupancyRepository
                .findActiveOccupancies(tableIds, date, time));
    }

    @Async
    @Override
    public CompletableFuture<List<TableOccupancy>> getActiveOccupancies(LocalDate date, List<String> tableIds) {
        return CompletableFuture.completedFuture(tableOccupancyRepository
                .findActiveOccupancies(date, tableIds));
    }

    @Async
    @Override
    public CompletableFuture<List<Reservation>> getActiveReservations(LocalDate date, LocalTime time, List<String> tableIds) {
        return CompletableFuture.completedFuture(reservationRepository
                .findActiveReservations(date, time, tableIds));
    }

    @Async
    @Override
    public CompletableFuture<List<Reservation>> getOverlappingReservations(LocalDate date, LocalTime startTime, LocalTime endTime, List<String> tableIds) {
        return CompletableFuture.completedFuture(
                reservationRepository.findOverlappingReservations(tableIds, date, startTime, endTime)
        );
    }

    @Async
    @Override
    public CompletableFuture<List<Reservation>> getUpcomingReservations(LocalDate date, LocalTime time, LocalTime futureTime, List<String> tableIds) {
        return CompletableFuture.completedFuture(reservationRepository
                .findUpcomingReservations(date, time, futureTime, tableIds));
    }
}
