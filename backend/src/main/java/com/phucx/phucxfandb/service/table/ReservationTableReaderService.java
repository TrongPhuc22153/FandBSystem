package com.phucx.phucxfandb.service.table;

import com.phucx.phucxfandb.constant.TableStatus;
import com.phucx.phucxfandb.dto.request.TableRequestParamDTO;
import com.phucx.phucxfandb.dto.response.ReservationTableDTO;
import com.phucx.phucxfandb.entity.ReservationTable;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationTableReaderService {
    Page<ReservationTableDTO> getReservationTables(TableRequestParamDTO params);
    ReservationTableDTO getReservationTable(String tableId);
    ReservationTable getReservationTableEntity(String tableId);
    ReservationTable getAvailableTable(int numberOfGuests, LocalDateTime requestedStartTime, LocalDateTime requestedEndTime);

    List<ReservationTable> getTablesEntities(TableStatus status, int numberOfGuest, LocalDateTime startTime, LocalDateTime endTime);

}