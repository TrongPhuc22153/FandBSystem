package com.phucx.phucxfandb.service.table.impl;

import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.entity.TableEntity;
import com.phucx.phucxfandb.entity.TableOccupancy;
import com.phucx.phucxfandb.enums.TableOccupancyStatus;
import com.phucx.phucxfandb.dto.request.RequestTableOccupancyDTO;
import com.phucx.phucxfandb.dto.response.TableOccupancyDTO;
import com.phucx.phucxfandb.entity.Employee;
import com.phucx.phucxfandb.exception.*;
import com.phucx.phucxfandb.mapper.TableOccupancyMapper;
import com.phucx.phucxfandb.repository.ReservationRepository;
import com.phucx.phucxfandb.repository.TableOccupancyRepository;
import com.phucx.phucxfandb.repository.TableRepository;
import com.phucx.phucxfandb.service.employee.EmployeeReaderService;
import com.phucx.phucxfandb.service.table.TableOccupancyUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import static com.phucx.phucxfandb.constant.TableConstant.CLEANUP_BUFFER_MINUTES;
import static com.phucx.phucxfandb.constant.TableConstant.DEFAULT_WALK_IN_DURATION_MINUTES;

@Slf4j
@Service
@RequiredArgsConstructor
public class TableOccupancyUpdateServiceImpl implements TableOccupancyUpdateService {
    private final EmployeeReaderService employeeReaderService;
    private final ReservationRepository reservationRepository;
    private final TableOccupancyRepository tableOccupancyRepository;
    private final TableRepository tableRepository;
    private final TableOccupancyMapper mapper;

    @Override
    @Transactional
    public TableOccupancyDTO createTableOccupancy(Authentication authentication, RequestTableOccupancyDTO createRequest) {
        String username = authentication.getName();
        Employee employee = employeeReaderService.getEmployeeEntityByUsername(username);

        TableOccupancy tableOccupancy = mapper.toTableOccupancy(createRequest, employee);
        TableOccupancy saved = tableOccupancyRepository.save(tableOccupancy);
        return mapper.toTableOccupancyDTO(saved);
    }

    @Override
    @Transactional
    public TableOccupancyDTO updateTableOccupancyStatus(String id, RequestTableOccupancyDTO requestTableOccupancyDTO) {
        TableOccupancyStatus status = requestTableOccupancyDTO.getStatus();
        if(TableOccupancyStatus.SEATED.equals(status)){
            return seatWalkIn(id, requestTableOccupancyDTO.getTableId());
        }else if(TableOccupancyStatus.COMPLETED.equals(status)){
            return finishOccupancy(id);
        }else{
            throw new IllegalStateException("Cannot change this table occupancy");
        }
    }

    @Override
    @Transactional
    public void updateTableOccupancyStatus(String occupancyId, String tableId, TableOccupancyStatus status) {
        if(TableOccupancyStatus.SEATED.equals(status)){
            seatWalkIn(occupancyId, tableId);
        }else if(TableOccupancyStatus.COMPLETED.equals(status)){
            finishOccupancy(occupancyId);
        }else{
            handleUpdateStatus(occupancyId, status);
        }
    }

    private TableOccupancyDTO seatWalkIn(String tableOccupancyId, String tableId) {
        TableOccupancy tableOccupancy = tableOccupancyRepository.findById(tableOccupancyId)
                .orElseThrow(() -> new NotFoundException(TableOccupancy.class.getSimpleName(), "id", tableOccupancyId));

        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new NotFoundException(TableEntity.class.getSimpleName(), "id", tableId));

        if (table.getCapacity() < tableOccupancy.getPartySize()) {
            throw new InsufficientPartySizeException("Party size does not match table capacity or minimum requirements");
        }

        if(!TableOccupancyStatus.WAITING.equals(tableOccupancy.getStatus())){
            throw new TableOccupiedException("Table is currently occupied");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalTime walkInStartTime = now.toLocalTime();
        LocalTime walkInEndTime = walkInStartTime.plusMinutes(DEFAULT_WALK_IN_DURATION_MINUTES);
        List<Reservation> upcomingReservations = reservationRepository.findOverlappingReservations(
                tableId, today, walkInStartTime, walkInEndTime);

        if (!upcomingReservations.isEmpty()) {
            Reservation earliestReservation = upcomingReservations.stream()
                    .min(Comparator.comparing(r -> r.getDate().atTime(r.getStartTime())))
                    .get();
            LocalDateTime reservationStart = earliestReservation.getDate().atTime(earliestReservation.getStartTime());
            long minutesUntilReservation = ChronoUnit.MINUTES.between(now, reservationStart);
            long availableMinutes = minutesUntilReservation - CLEANUP_BUFFER_MINUTES;

            if (availableMinutes <= 0) {
                throw  new TableOccupiedException("Table is reserved soon and cannot be seated");
            }
            if (availableMinutes < DEFAULT_WALK_IN_DURATION_MINUTES) {
                throw new InsufficientTimeException(
                        "Table is available but only for " + availableMinutes + " minutes due to an upcoming reservation",
                        (int) availableMinutes);
            }
        }

        tableOccupancy.setTable(table);
        tableOccupancy.setStatus(TableOccupancyStatus.SEATED);
        tableOccupancy.setDate(now.toLocalDate());
        tableOccupancy.setStartTime(now.toLocalTime());

        TableOccupancy saved = tableOccupancyRepository.save(tableOccupancy);
        return mapper.toTableOccupancyDTO(saved);
    }

    private TableOccupancyDTO finishOccupancy(String id){
        TableOccupancy tableOccupancy = tableOccupancyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TableOccupancy.class.getSimpleName(), "id", id));

        if(tableOccupancy.getTable() == null){
            throw new IllegalStateException("Table is missing");
        }

        TableOccupancyStatus status = tableOccupancy.getStatus();
        if(!(TableOccupancyStatus.SEATED.equals(status) || TableOccupancyStatus.CLEANING.equals(status))){
            throw new IllegalStateException("Table cannot be set as completed");
        }
        tableOccupancy.setEndTime(LocalDateTime.now().toLocalTime());
        tableOccupancy.setStatus(TableOccupancyStatus.COMPLETED);
        TableOccupancy updated = tableOccupancyRepository.save(tableOccupancy);
        return mapper.toTableOccupancyDTO(updated);
    }

    private void handleUpdateStatus(String id, TableOccupancyStatus status){
        TableOccupancy tableOccupancy = tableOccupancyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TableOccupancy.class.getSimpleName(), "id", id));

        tableOccupancy.setStatus(status);
        tableOccupancyRepository.save(tableOccupancy);
    }

    @Override
    @Transactional
    public TableOccupancyDTO updateTableOccupancy(String id, RequestTableOccupancyDTO updateRequest) {
        TableOccupancy tableOccupancy = tableOccupancyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TableOccupancy.class.getSimpleName(), "id", id));
        mapper.updateTableOccupancy(updateRequest, tableOccupancy);
        TableOccupancy updated = tableOccupancyRepository.save(tableOccupancy);
        return mapper.toTableOccupancyDTO(updated);
    }
}
