package com.phucx.phucxfandb.service.reservation;

import com.phucx.phucxfandb.constant.ReservationStatus;
import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;

public interface ReservationUpdateService {
    ReservationDTO createCustomerReservation(String username, RequestReservationDTO reservationDTO);
    ReservationDTO createEmployeeReservation(String username, RequestReservationDTO reservationDTO);

    ReservationDTO updateReservation(String username, String reservationId, RequestReservationDTO reservationDTO);
    ReservationDTO updateReservationStatus(String reservationId, ReservationStatus status);
//    ReservationDTO updateReservationStatus(String reservationId, ReservationStatus status);
//    void cancelReservation(String reservationId);
//    void cancelReservation(String username, String reservationId);
    ReservationDTO createReservationAndAssignTable(String username, RequestReservationDTO requestReservationDTO);

}
