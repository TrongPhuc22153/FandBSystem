package com.phucx.phucxfandb.service.reservation;

import com.phucx.phucxfandb.dto.request.ReservationRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationReaderService {
    Page<ReservationDTO> getReservations(ReservationRequestParamsDTO params, Authentication authentication);
    ReservationDTO getReservation(String reservationId);
    Reservation getReservationEntity(String reservationId);

    List<Reservation> getOverDueReservations(LocalDateTime dateTime);

}
