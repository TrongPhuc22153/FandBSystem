package com.phucx.phucxfandb.service.table;

import com.phucx.phucxfandb.constant.ReservationStatus;
import com.phucx.phucxfandb.constant.TableStatus;
import com.phucx.phucxfandb.dto.response.ReservationTableDTO;
import com.phucx.phucxfandb.entity.ReservationTable;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationTableReaderService {
    Page<ReservationTableDTO> getReservationTables(int pageNumber, int pageSize);
    ReservationTableDTO getReservationTable(String tableId);
    ReservationTableDTO getReservationTable(int tableNumber);
    ReservationTable getReservationTableEntity(String tableId);
    ReservationTable getAvailableTable(int numberOfGuests, LocalDateTime requestedStartTime, LocalDateTime requestedEndTime);
//    Page<ReservationTableDTO> getReservedTables(LocalDate date, int pageNumber, int pageSize);
    Page<ReservationTableDTO> getReservedTables(ReservationStatus status, int pageNumber, int pageSize);

    List<ReservationTable> getTablesEntities(TableStatus status, int numberOfGuest, LocalDateTime startTime, LocalDateTime endTime);
    List<ReservationTable> getTablesEntities(TableStatus status);
    List<ReservationTableDTO> getTables(TableStatus status);

}