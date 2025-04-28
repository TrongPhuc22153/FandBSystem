package com.phucx.phucxfandb.service.table.impl;

import com.phucx.phucxfandb.constant.ReservationStatus;
import com.phucx.phucxfandb.constant.TableStatus;
import com.phucx.phucxfandb.dto.response.ReservationTableDTO;
import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.entity.ReservationTable;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.ReservationTableMapper;
import com.phucx.phucxfandb.repository.ReservationRepository;
import com.phucx.phucxfandb.repository.ReservationTableRepository;
import com.phucx.phucxfandb.service.table.ReservationTableReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationTableReaderServiceImpl implements ReservationTableReaderService {
    private final ReservationTableRepository reservationTableRepository;
    private final ReservationTableMapper mapper;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationTableDTO> getReservationTables(int pageNumber, int pageSize) {
        log.info("getReservationTables(pageNumber={}, pageSize={})", pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<ReservationTable> reservationTables = reservationTableRepository.findByIsDeletedFalse(page);
        return reservationTables.map(mapper::toReservationTableDTO);
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
    public ReservationTableDTO getReservationTable(int tableNumber) {
        log.info("getReservationTable(tableNumber={})", tableNumber);
        ReservationTable reservationTable = reservationTableRepository.findByTableNumberAndIsDeletedFalse(tableNumber)
                .orElseThrow(() -> new NotFoundException("ReservationTable", tableNumber));
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
    public Page<ReservationTableDTO> getReservedTables(ReservationStatus status, int pageNumber, int pageSize) {
        log.info("getReservedTables(status={}, pageNumber={}, pageSize={})", status, pageNumber, pageSize);
        Page<Reservation> reservations = reservationRepository
                .findByStatus(status, PageRequest.of(pageNumber, pageSize));
        return reservations.map(Reservation::getTable).map(mapper::toReservationTableDTO);
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
        List<ReservationTable> tables = reservationTableRepository.findFirstAvailableTableWithLeastCapacity(numberOfGuests, requestedStartTime, requestedEndTime);
        if(tables.isEmpty()) throw new NotFoundException("No available table found for the requested time and number of guests.");
        return tables.get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationTable> getTablesEntities(TableStatus status) {
        return reservationTableRepository.findByStatusAndIsDeletedFalse(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationTableDTO> getTables(TableStatus status) {
        return reservationTableRepository.findByStatusAndIsDeletedFalse(status)
                .stream().map(mapper::toReservationTableDTO)
                .collect(Collectors.toList());
    }
}