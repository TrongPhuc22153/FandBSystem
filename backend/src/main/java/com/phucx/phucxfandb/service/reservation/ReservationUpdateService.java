package com.phucx.phucxfandb.service.reservation;

import com.phucx.phucxfandb.constant.ReservationStatus;
import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;

public interface ReservationUpdateService {
    ReservationDTO createCustomerReservation(String username, RequestReservationDTO reservationDTO);
    ReservationDTO createEmployeeReservation(String username, RequestReservationDTO reservationDTO);

    ReservationDTO updateReservationStatus(String reservationId, ReservationStatus status);
    ReservationDTO updateReservationStatusByCustomer(String username, String reservationId, ReservationStatus status);
    ReservationDTO updateReservationStatusByEmployee(String username, String reservationId, ReservationStatus status);
}
