package com.phucx.phucxfandb.service.table.impl;

import com.phucx.phucxfandb.constant.TableStatus;
import com.phucx.phucxfandb.dto.request.TableRequestParamDTO;
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
    public Page<ReservationTableDTO> getReservationTables(TableRequestParamDTO params) {
        log.info("getReservationTables(params={})", params);
        Pageable page = PageRequest.of(params.getPage(), params.getSize(), Sort.by(params.getDirection(), params.getField()));
        Specification<ReservationTable> spec = Specification
                .where(TableSpecification.hasIsDeleted(params.getIsDeleted()))
                .and(TableSpecification.hasTableNumber(params.getTableNumber()))
                .and(TableSpecification.hasStatus(params.getStatus()));
        return reservationTableRepository.findAll(spec, page)
                .map(mapper::toReservationTableDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationTableDTO getReservationTable(String tableId) {
        log.info("getReservationTable(tableId={})", tableId);
        ReservationTable reservationTable = reservationTableRepository.findByTableIdAndIsDeletedFalse(tableId)
                .orElseThrow(() -> new NotFoundException("ReservationTable", tableId));
        return mapper.toReservationTableDTO(reservationTable);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationTable getReservationTableEntity(String tableId) {
        log.info("getReservationTableEntity(tableId={})", tableId);
        return reservationTableRepository.findByTableIdAndIsDeletedFalse(tableId)
                .orElseThrow(() -> new NotFoundException("ReservationTable", tableId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationTable> getTablesEntities(TableStatus status, int numberOfGuests, LocalDateTime startTime, LocalDateTime endTime) {
        return reservationTableRepository.findAvailableTablesForReservation(
                status, numberOfGuests, startTime, endTime
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationTable getAvailableTable(int numberOfGuests, LocalDateTime requestedStartTime, LocalDateTime requestedEndTime) {
        log.info("getAvailableTable(numberOfGuests={}, requestedStartTime={}, requestedEndTime={})", numberOfGuests, requestedStartTime, requestedEndTime);
        List<ReservationTable> tables = reservationTableRepository
                .findFirstAvailableTableWithLeastCapacity(numberOfGuests, Boolean.FALSE, requestedStartTime, requestedEndTime);
        if(tables.isEmpty()) throw new NotFoundException("No available table found for the requested time and number of guests.");
        return tables.get(0);
    }
}