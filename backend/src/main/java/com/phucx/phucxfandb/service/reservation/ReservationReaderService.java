package com.phucx.phucxfandb.service.reservation;

import com.phucx.phucxfandb.dto.request.ReservationRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface ReservationReaderService {
    Page<ReservationDTO> getReservations(ReservationRequestParamsDTO params, Authentication authentication);
    ReservationDTO getReservation(String reservationId);
    Reservation getReservationEntity(String reservationId);

}
