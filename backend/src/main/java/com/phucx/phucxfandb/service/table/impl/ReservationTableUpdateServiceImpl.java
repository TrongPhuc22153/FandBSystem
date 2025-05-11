package com.phucx.phucxfandb.service.table.impl;

import com.phucx.phucxfandb.constant.TableStatus;
import com.phucx.phucxfandb.dto.request.RequestReservationTableDTO;
import com.phucx.phucxfandb.dto.response.ReservationTableDTO;
import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.entity.ReservationTable;
import com.phucx.phucxfandb.exception.EntityExistsException;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.ReservationTableMapper;
import com.phucx.phucxfandb.repository.ReservationRepository;
import com.phucx.phucxfandb.repository.ReservationTableRepository;
import com.phucx.phucxfandb.service.table.ReservationTableUpdateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationTableUpdateServiceImpl implements ReservationTableUpdateService {
    private final ReservationTableRepository reservationTableRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationTableMapper mapper;

    @Override
    @Modifying
    @Transactional
    public void deleteReservationTable(String tableId) {
        log.info("deleteReservationTable(tableId={})", tableId);
        ReservationTable existingReservationTable = reservationTableRepository.findByTableIdAndIsDeletedFalse(tableId)
                .orElseThrow(() -> new NotFoundException("ReservationTable", "id", tableId));
        existingReservationTable.setIsDeleted(Boolean.TRUE);
        reservationTableRepository.save(existingReservationTable);
    }

    @Override
    @Modifying
    @Transactional
    public ReservationTableDTO updateTableStatus(String tableId, RequestReservationTableDTO requestReservationTableDTO) {
        log.info("updateTableStatus(id={}, requestReservationTableDTO={})", tableId, requestReservationTableDTO);
        ReservationTable existingReservationTable = reservationTableRepository.findById(tableId)
                .orElseThrow(() -> new NotFoundException("ReservationTable", "id", tableId));
        if(requestReservationTableDTO.getStatus()!=null)
            existingReservationTable.setStatus(requestReservationTableDTO.getStatus());
        if(requestReservationTableDTO.getIsDeleted()!=null) {
            existingReservationTable.setIsDeleted(requestReservationTableDTO.getIsDeleted());
        }
        ReservationTable updatedReservationTable = reservationTableRepository.save(existingReservationTable);
        return mapper.toReservationTableDTO(updatedReservationTable);
    }

    @Override
    @Modifying
    @Transactional
    public ReservationTableDTO updateTableStatus(String tableId, TableStatus status) {
        log.info("updateTableStatus(id={}, status={})", tableId, status);
        ReservationTable existingReservationTable = reservationTableRepository.findByTableIdAndIsDeletedFalse(tableId)
                .orElseThrow(() -> new NotFoundException("ReservationTable", "id", tableId));
        existingReservationTable.setStatus(status);
        ReservationTable updatedReservationTable = reservationTableRepository.save(existingReservationTable);
        return mapper.toReservationTableDTO(updatedReservationTable);
    }

    @Override
    @Modifying
    @Transactional
    public void unassignTable(String reservationId) {
        log.info("unassignTable(reservationId={})", reservationId);
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation", "id", reservationId));
        ReservationTable assignedTable = reservation.getTable();
        if (assignedTable != null) {
            assignedTable.setStatus(TableStatus.UNOCCUPIED);
            reservationTableRepository.save(assignedTable);
            reservation.setTable(null);
            reservationRepository.save(reservation);
        }
    }

    @Override
    @Modifying
    @Transactional
    public ReservationTableDTO updateReservationTable(String tableId, RequestReservationTableDTO requestReservationTableDTO) {
        log.info("updateReservationTable(id={}, requestReservationTableDTO={})", tableId, requestReservationTableDTO);
        ReservationTable existingReservationTable = reservationTableRepository.findByTableIdAndIsDeletedFalse(tableId)
                .orElseThrow(() -> new NotFoundException("ReservationTable", tableId));
        mapper.updateReservationTable(requestReservationTableDTO, existingReservationTable);
        ReservationTable updatedReservationTable = reservationTableRepository.save(existingReservationTable);
        return mapper.toReservationTableDTO(updatedReservationTable);
    }

    @Override
    @Modifying
    @Transactional
    public ReservationTableDTO seatDineInCustomer(int numberOfGuests) {
        log.info("seatDineInCustomer(numberOfGuest={})", numberOfGuests);
        List<ReservationTable> availableTables = reservationTableRepository.findTableWithLeastCapacity(
                numberOfGuests, TableStatus.UNOCCUPIED
        );

        Optional<ReservationTable> suitableTable = availableTables.stream()
                .findFirst();

        if (suitableTable.isPresent()) {
            ReservationTable tableToSeat = suitableTable.get();
            tableToSeat.setStatus(TableStatus.OCCUPIED);
            ReservationTable updated = reservationTableRepository.save(tableToSeat);
            return mapper.toReservationTableDTO(updated);
        } else {
            throw new NotFoundException("No suitable table available for a party of " + numberOfGuests + " at this time.");
        }
    }

    @Override
    @Modifying
    @Transactional
    public ReservationTableDTO createReservationTable(RequestReservationTableDTO createReservationTableDTO) {
        log.info("createReservationTable(createReservationTableDTO={})", createReservationTableDTO);
        if (reservationTableRepository.existsByTableNumber(createReservationTableDTO.getTableNumber())) {
            throw new EntityExistsException("ReservationTable " + createReservationTableDTO.getTableNumber() + " already exists");
        }
        ReservationTable reservationTable = mapper.toReservationTable(createReservationTableDTO);
        ReservationTable savedReservationTable = reservationTableRepository.save(reservationTable);
        return mapper.toReservationTableDTO(savedReservationTable);
    }

    @Override
    @Modifying
    @Transactional
    public List<ReservationTableDTO> createReservationTables(List<RequestReservationTableDTO> createReservationTableDTOs) {
        log.info("createReservationTables(createReservationTableDTOs={})", createReservationTableDTOs);
        List<ReservationTable> reservationTablesToSave = createReservationTableDTOs.stream()
                .map(mapper::toReservationTable)
                .collect(Collectors.toList());
        return reservationTableRepository.saveAll(reservationTablesToSave).stream()
                .map(mapper::toReservationTableDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Modifying
    @Transactional
    public void deleteTable(String tableId) {
        log.info("deleteTable(id={})", tableId);
        ReservationTable existingReservationTable = reservationTableRepository.findByTableIdAndIsDeletedFalse(tableId)
                .orElseThrow(() -> new NotFoundException("ReservationTable", "id", tableId));
        existingReservationTable.setIsDeleted(Boolean.TRUE);
        reservationTableRepository.save(existingReservationTable);
    }
}