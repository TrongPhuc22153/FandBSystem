package com.phucx.phucxfandb.service.table.impl;

import com.phucx.phucxfandb.constant.TableStatus;
import com.phucx.phucxfandb.constant.WaitListStatus;
import com.phucx.phucxfandb.dto.request.RequestReservationTableDTO;
import com.phucx.phucxfandb.dto.response.ReservationTableDTO;
import com.phucx.phucxfandb.entity.ReservationTable;
import com.phucx.phucxfandb.exception.EntityExistsException;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.ReservationTableMapper;
import com.phucx.phucxfandb.repository.ReservationTableRepository;
import com.phucx.phucxfandb.service.table.ReservationTableUpdateService;
import com.phucx.phucxfandb.service.waitlist.WaitListReaderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationTableUpdateServiceImpl implements ReservationTableUpdateService {
    private final ReservationTableRepository reservationTableRepository;
    private final WaitListReaderService waitListReaderService;
    private final ReservationTableMapper mapper;

    @Override
    @Transactional
    public ReservationTableDTO updateTableStatus(String tableId, RequestReservationTableDTO requestReservationTableDTO) {
        ReservationTable existingReservationTable = reservationTableRepository.findById(tableId)
                .orElseThrow(() -> new NotFoundException(ReservationTable.class.getSimpleName(), "id", tableId));
        TableStatus status = requestReservationTableDTO.getStatus();
        Boolean isDeleted = requestReservationTableDTO.getIsDeleted();
        if(status!=null) {
            boolean hasSeatedWaitLists = waitListReaderService.existsByTableIdAndWaitListStatus(
                    tableId, WaitListStatus.SEATED
            );
            if (hasSeatedWaitLists && status != TableStatus.OCCUPIED) {
                throw new IllegalStateException("Cannot change table status: SEATED waitlist entries exist for this table");
            }

            existingReservationTable.setStatus(status);
        }
        if(isDeleted!=null) {
            existingReservationTable.setIsDeleted(isDeleted);
        }
        ReservationTable updatedReservationTable = reservationTableRepository.save(existingReservationTable);
        return mapper.toReservationTableDTO(updatedReservationTable);
    }

    @Override
    @Transactional
    public ReservationTableDTO updateTableStatus(String tableId, TableStatus status) {
        ReservationTable existingReservationTable = reservationTableRepository.findByTableIdAndIsDeletedFalse(tableId)
                .orElseThrow(() -> new NotFoundException(ReservationTable.class.getSimpleName(), "id", tableId));
        existingReservationTable.setStatus(status);
        ReservationTable updatedReservationTable = reservationTableRepository.save(existingReservationTable);
        return mapper.toReservationTableDTO(updatedReservationTable);
    }

    @Override
    @Transactional
    public ReservationTableDTO updateReservationTable(String tableId, RequestReservationTableDTO requestReservationTableDTO) {
        ReservationTable existingReservationTable = reservationTableRepository.findByTableIdAndIsDeletedFalse(tableId)
                .orElseThrow(() -> new NotFoundException(ReservationTable.class.getSimpleName(), tableId));
        mapper.updateReservationTable(requestReservationTableDTO, existingReservationTable);
        ReservationTable updatedReservationTable = reservationTableRepository.save(existingReservationTable);
        return mapper.toReservationTableDTO(updatedReservationTable);
    }

    @Override
    @Transactional
    public ReservationTableDTO createReservationTable(RequestReservationTableDTO createReservationTableDTO) {
        if (reservationTableRepository.existsByTableNumber(createReservationTableDTO.getTableNumber())) {
            throw new EntityExistsException("ReservationTable " + createReservationTableDTO.getTableNumber() + " already exists");
        }
        ReservationTable reservationTable = mapper.toReservationTable(createReservationTableDTO);
        ReservationTable savedReservationTable = reservationTableRepository.save(reservationTable);
        return mapper.toReservationTableDTO(savedReservationTable);
    }

    @Override
    @Transactional
    public List<ReservationTableDTO> createReservationTables(List<RequestReservationTableDTO> createReservationTableDTOs) {
        List<ReservationTable> reservationTablesToSave = createReservationTableDTOs.stream()
                .map(mapper::toReservationTable)
                .collect(Collectors.toList());
        return reservationTableRepository.saveAll(reservationTablesToSave).stream()
                .map(mapper::toReservationTableDTO)
                .collect(Collectors.toList());
    }
}