package com.phucx.phucxfandb.service.reservation;

import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;

public interface ReservationProcessingService {
    ReservationDTO confirmReservation(String username, String reservationId);
    void cancelReservation(String username, String reservationId);
    ReservationDTO markReservationAsReceived(String username, String reservationId);
    ReservationDTO placeCustomerReservation(String username, RequestReservationDTO requestReservationDTO);
    ReservationDTO placeEmployeeReservation(String username, RequestReservationDTO requestReservationDTO);
    ReservationDTO completeReservation(String username, String reservationId);

    ReservationDTO markReservationAsPrepared(String reservationId);
}
