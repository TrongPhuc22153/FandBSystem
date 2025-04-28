package com.phucx.phucxfandb.service.reservation.imp;

import com.phucx.phucxfandb.constant.ReservationStatus;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.ReservationMapper;
import com.phucx.phucxfandb.repository.ReservationRepository;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationReaderServiceImp implements ReservationReaderService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDTO> getAllReservations(LocalDate date, ReservationStatus status, int pageNumber, int pageSize) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDTO> getReservations(ReservationStatus status, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return reservationRepository.findByStatus(status, pageable)
                .map(reservationMapper::toReservationDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDTO> getAllReservations(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return reservationRepository.findAll(pageable)
                .map(reservationMapper::toReservationDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationDTO getReservation(String reservationId) {
        var reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation", reservationId));
        return reservationMapper.toReservationDTO(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation getReservationEntity(String reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation", reservationId));
    }
}
