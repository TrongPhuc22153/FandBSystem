package com.phucx.phucxfandb.service.reservation;

import com.phucx.phucxfandb.enums.ReservationAction;
import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import org.springframework.security.core.Authentication;

public interface ReservationProcessingService {
    ReservationDTO cancelReservation(Authentication authentication, String reservationId);

    ReservationDTO placeCustomerReservation(String username, RequestReservationDTO requestReservationDTO);
    ReservationDTO placeEmployeeReservation(String username, RequestReservationDTO requestReservationDTO);
    ReservationDTO placeReservation(RequestReservationDTO requestReservationDTO, Authentication authentication);

    ReservationDTO markReservationAsServed(Authentication authentication, String reservationId);

    ReservationDTO markReservationAsReady(Authentication authentication, String reservationId);

    ReservationDTO completeReservation(String username, String reservationId);

    ReservationDTO preparingReservation(String username, String reservationId);

    ReservationDTO markReservationAsPrepared(String username, String reservationId);

    ReservationDTO processReservation(Authentication authentication, String reservationId, ReservationAction action);
}
