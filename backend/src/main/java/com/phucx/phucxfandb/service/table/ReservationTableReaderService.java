package com.phucx.phucxfandb.service.table;

import com.phucx.phucxfandb.dto.request.TableRequestParamsDTODTO;
import com.phucx.phucxfandb.dto.response.ReservationTableDTO;
import com.phucx.phucxfandb.entity.ReservationTable;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface ReservationTableReaderService {
    Page<ReservationTableDTO> getReservationTables(TableRequestParamsDTODTO params);
    ReservationTableDTO getReservationTable(String tableId);
    ReservationTable getReservationTableEntity(String tableId);
    ReservationTable getAvailableTable(int numberOfGuests, LocalDateTime requestedStartTime, LocalDateTime requestedEndTime);
}