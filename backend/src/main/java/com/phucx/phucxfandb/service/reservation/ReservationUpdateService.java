package com.phucx.phucxfandb.service.reservation;

import com.phucx.phucxfandb.enums.PaymentStatus;
import com.phucx.phucxfandb.enums.ReservationStatus;
import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;

public interface ReservationUpdateService {
    ReservationDTO createCustomerReservation(String username, RequestReservationDTO reservationDTO);
    ReservationDTO createEmployeeReservation(String username, RequestReservationDTO reservationDTO);

    ReservationDTO updateReservation(String username, String reservationId, RequestReservationDTO request);
    ReservationDTO updateReservation(String reservationId, ReservationStatus reservationStatus, PaymentStatus paymentStatus);
    ReservationDTO updateReservationStatus(String reservationId, ReservationStatus status);
    ReservationDTO updateReservationStatusByCustomer(String username, String reservationId, ReservationStatus status);
    ReservationDTO updateReservationStatusByEmployee(String username, String reservationId, ReservationStatus status);
}
