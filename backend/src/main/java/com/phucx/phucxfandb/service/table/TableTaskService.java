package com.phucx.phucxfandb.service.table;

import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.entity.TableOccupancy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TableTaskService {

    CompletableFuture<List<TableOccupancy>> getActiveOccupancies(LocalDate date, LocalTime time, List<String> tableIds);
    CompletableFuture<List<TableOccupancy>> getActiveOccupancies(LocalDate date, List<String> tableIds);
    CompletableFuture<List<Reservation>> getActiveReservations(LocalDate date, LocalTime time, List<String> tableIds);
    CompletableFuture<List<Reservation>> getOverlappingReservations(LocalDate date, LocalTime startTime, LocalTime endTime, List<String> tableIds);
    CompletableFuture<List<Reservation>> getUpcomingReservations(LocalDate date, LocalTime time, LocalTime futureTime, List<String> tableIds);

}
