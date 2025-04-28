package com.phucx.phucxfandb.service.table;

import com.phucx.phucxfandb.constant.TableStatus;
import com.phucx.phucxfandb.dto.request.RequestReservationTableDTO;
import com.phucx.phucxfandb.dto.response.ReservationTableDTO;
import java.util.List;

public interface ReservationTableUpdateService {

    void deleteReservationTable(String tableId);
    // update
    ReservationTableDTO updateTableStatus(String tableId, TableStatus status);
    ReservationTableDTO updateReservationTable(String tableId, RequestReservationTableDTO requestReservationTableDTO);
    // create
    ReservationTableDTO seatDineInCustomer(int numberOfGuests);
    void unassignTable(String reservationId);
    ReservationTableDTO createReservationTable(RequestReservationTableDTO createReservationTableDTO);
    List<ReservationTableDTO> createReservationTables(List<RequestReservationTableDTO> createReservationTableDTOs);
}