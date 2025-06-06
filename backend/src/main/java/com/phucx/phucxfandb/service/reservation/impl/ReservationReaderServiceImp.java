package com.phucx.phucxfandb.service.reservation.impl;

import com.phucx.phucxfandb.enums.RoleName;
import com.phucx.phucxfandb.dto.request.ReservationRequestParamsDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.ReservationMapper;
import com.phucx.phucxfandb.repository.ReservationRepository;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import com.phucx.phucxfandb.specifications.ReservationSpecification;
import com.phucx.phucxfandb.utils.RoleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationReaderServiceImp implements ReservationReaderService {
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDTO> getReservations(ReservationRequestParamsDTO params, Authentication authentication) {
        List<RoleName> roles = RoleUtils.getRoles(authentication.getAuthorities());
        if(roles.contains(RoleName.CUSTOMER)){
            return getCustomerReservations(authentication.getName(), params);
        }else {
            return getReservations(params);
        }
    }

    public Page<ReservationDTO> getCustomerReservations(String username, ReservationRequestParamsDTO params){
        Pageable pageable = PageRequest.of(
                params.getPage(),
                params.getSize(),
                Sort.by(params.getDirection(), params.getField())
        );
        Specification<Reservation> spec = Specification
                .where(ReservationSpecification.hasCustomerUsername(username))
                .and(ReservationSpecification.hasStatuses(params.getStatus()))
                .and(ReservationSpecification.hasDate(params.getStartDate(), params.getEndDate()));

        return reservationRepository.findAll(spec, pageable)
                .map(reservationMapper::toReservationListEntryDTO);
    }

    public Page<ReservationDTO> getReservations(ReservationRequestParamsDTO params){
        Pageable pageable = PageRequest.of(
                params.getPage(),
                params.getSize(),
                Sort.by(params.getDirection(), params.getField())
        );
        Specification<Reservation> spec = Specification
                .where(ReservationSpecification.hasStatuses(params.getStatus()))
                .and(ReservationSpecification.hasDate(params.getStartDate(), params.getEndDate()));

        return reservationRepository.findAll(spec, pageable)
                .map(reservationMapper::toReservationListEntryDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationDTO getReservation(String reservationId) {
        return reservationRepository.findById(reservationId)
                .map(reservationMapper::toReservationDTO)
                .orElseThrow(() -> new NotFoundException(Reservation.class.getSimpleName(), "id", reservationId));
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation getReservationEntity(String reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(Reservation.class.getSimpleName(), "id", reservationId));
    }
}
