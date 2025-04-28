package com.phucx.phucxfandb.service.reservation;

import com.phucx.phucxfandb.constant.ReservationStatus;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.entity.Reservation;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface ReservationReaderService {
    Page<ReservationDTO> getAllReservations(LocalDate date, ReservationStatus status, int pageNumber, int pageSize);
    Page<ReservationDTO> getReservations(ReservationStatus status, int pageNumber, int pageSize);
    Page<ReservationDTO> getAllReservations(int pageNumber, int pageSize);
    ReservationDTO getReservation(String reservationId);
    Reservation getReservationEntity(String reservationId);

}
