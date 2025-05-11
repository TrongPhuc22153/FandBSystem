package com.phucx.phucxfandb.service.reservation;

import com.phucx.phucxfandb.constant.ReservationAction;
import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import org.springframework.security.core.Authentication;

public interface ReservationProcessingService {
    ReservationDTO confirmReservation(String username, String reservationId);
    ReservationDTO cancelReservation(String username, String reservationId);
    ReservationDTO markReservationAsReceived(String username, String reservationId);
    ReservationDTO placeCustomerReservation(String username, RequestReservationDTO requestReservationDTO);
    ReservationDTO placeEmployeeReservation(String username, RequestReservationDTO requestReservationDTO);
    ReservationDTO completeReservation(String username, String reservationId);


    ReservationDTO processReservation(String username, String reservationId, ReservationAction action);
    ReservationDTO placeReservation(RequestReservationDTO requestReservationDTO, Authentication authentication);
    
    ReservationDTO preparingReservation(String username, String reservationId);

    ReservationDTO markReservationAsPrepared(String reservationId);
}
