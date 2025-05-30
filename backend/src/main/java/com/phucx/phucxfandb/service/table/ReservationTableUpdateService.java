package com.phucx.phucxfandb.service.table;

import com.phucx.phucxfandb.enums.TableStatus;
import com.phucx.phucxfandb.dto.request.RequestReservationTableDTO;
import com.phucx.phucxfandb.dto.response.ReservationTableDTO;
import java.util.List;

public interface ReservationTableUpdateService {

    ReservationTableDTO updateTableStatus(String tableId, RequestReservationTableDTO requestReservationTableDTO);
    ReservationTableDTO updateTableStatus(String tableId, TableStatus status);
    ReservationTableDTO updateReservationTable(String tableId, RequestReservationTableDTO requestReservationTableDTO);

    ReservationTableDTO createReservationTable(RequestReservationTableDTO createReservationTableDTO);
    List<ReservationTableDTO> createReservationTables(List<RequestReservationTableDTO> createReservationTableDTOs);
}