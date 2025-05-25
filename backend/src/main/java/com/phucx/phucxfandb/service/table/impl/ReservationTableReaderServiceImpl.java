package com.phucx.phucxfandb.service.table.impl;

import com.phucx.phucxfandb.dto.request.TableRequestParamsDTODTO;
import com.phucx.phucxfandb.dto.response.ReservationTableDTO;
import com.phucx.phucxfandb.entity.ReservationTable;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.ReservationTableMapper;
import com.phucx.phucxfandb.repository.ReservationTableRepository;
import com.phucx.phucxfandb.service.table.ReservationTableReaderService;
import com.phucx.phucxfandb.specifications.TableSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationTableReaderServiceImpl implements ReservationTableReaderService {
    private final ReservationTableRepository reservationTableRepository;
    private final ReservationTableMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationTableDTO> getReservationTables(TableRequestParamsDTODTO params) {
        Pageable page = PageRequest.of(params.getPage(), params.getSize(), Sort.by(params.getDirection(), params.getField()));
        Specification<ReservationTable> spec = Specification
                .where(TableSpecification.hasIsDeleted(params.getIsDeleted()))
                .and(TableSpecification.hasSearch(params.getSearch()))
                .and(TableSpecification.hasTableNumber(params.getTableNumber()))
                .and(TableSpecification.hasStatus(params.getStatus()));
        return reservationTableRepository.findAll(spec, page)
                .map(mapper::toReservationTableDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationTableDTO getReservationTable(String tableId) {
        ReservationTable reservationTable = reservationTableRepository.findByTableIdAndIsDeletedFalse(tableId)
                .orElseThrow(() -> new NotFoundException(ReservationTable.class.getSimpleName(), "id", tableId));
        return mapper.toReservationTableDTO(reservationTable);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationTable getReservationTableEntity(String tableId) {
        return reservationTableRepository.findByTableIdAndIsDeletedFalse(tableId)
                .orElseThrow(() -> new NotFoundException(ReservationTable.class.getSimpleName(), "id", tableId));
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationTable getAvailableTable(int numberOfGuests, LocalDateTime requestedStartTime, LocalDateTime requestedEndTime) {
        List<ReservationTable> tables = reservationTableRepository
                .findFirstAvailableTableWithLeastCapacity(numberOfGuests, Boolean.FALSE, requestedStartTime, requestedEndTime);
        if(tables.isEmpty()) throw new NotFoundException("No available table found for the requested time and number of guests.");
        return tables.get(0);
    }
}