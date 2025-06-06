package com.phucx.phucxfandb.service.table.impl;

import com.phucx.phucxfandb.constant.TableStatusConstant;
import com.phucx.phucxfandb.dto.request.AvailableTableRequestParamsDTO;
import com.phucx.phucxfandb.dto.request.TableRequestParamsDTODTO;
import com.phucx.phucxfandb.dto.response.TableDTO;
import com.phucx.phucxfandb.dto.response.TableSummaryDTO;
import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.entity.TableEntity;
import com.phucx.phucxfandb.entity.TableOccupancy;
import com.phucx.phucxfandb.enums.TableOccupancyStatus;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.mapper.TableMapper;
import com.phucx.phucxfandb.repository.ReservationRepository;
import com.phucx.phucxfandb.repository.TableOccupancyRepository;
import com.phucx.phucxfandb.repository.TableRepository;
import com.phucx.phucxfandb.service.table.TableReaderService;
import com.phucx.phucxfandb.specifications.TableSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.phucx.phucxfandb.constant.TableConstant.UPCOMING_RESERVATION_WINDOW_MINUTES;

@Slf4j
@Service
@RequiredArgsConstructor
public class TableReaderServiceImpl implements TableReaderService {
    private final TableOccupancyRepository tableOccupancyRepository;
    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final TableMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TableDTO> getTables(TableRequestParamsDTODTO params) {
        Pageable page = PageRequest.of(params.getPage(), params.getSize(), Sort.by(params.getDirection(), params.getField()));
        Specification<TableEntity> spec = Specification
                .where(TableSpecification.hasIsDeleted(params.getIsDeleted()))
                .and(TableSpecification.hasSearch(params.getSearch()))
                .and(TableSpecification.hasTableNumber(params.getTableNumber()));
        return tableRepository.findAll(spec, page)
                .map(mapper::toTableDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TableDTO getTable(String tableId) {
        TableEntity tableEntity = tableRepository.findByTableIdAndIsDeletedFalse(tableId)
                .orElseThrow(() -> new NotFoundException(TableEntity.class.getSimpleName(), "id", tableId));
        return mapper.toTableDTO(tableEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public TableEntity getTableEntity(String tableId) {
        return tableRepository.findByTableIdAndIsDeletedFalse(tableId)
                .orElseThrow(() -> new NotFoundException(TableEntity.class.getSimpleName(), "id", tableId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TableDTO> getTableAvailability(AvailableTableRequestParamsDTO params) {
        Pageable pageable = PageRequest.of(
                params.getPage(),
                params.getSize(),
                Sort.by(params.getDirection(), params.getField()));

        LocalDate date = params.getDate();
        LocalTime time = params.getTime();

        Specification<TableEntity> spec = Specification.where(TableSpecification.hasIsDeleted(false))
                .and(TableSpecification.hasTableNumber(params.getTableNumber()))
                .and(TableSpecification.hasSearch(params.getSearch()));

        Page<TableEntity> allTables = tableRepository.findAll(spec, pageable);
        List<String> tableIds = allTables.stream()
                .map(TableEntity::getTableId)
                .collect(Collectors.toList());

        List<TableOccupancy> activeOccupancies = tableOccupancyRepository
                .findActiveOccupancies(date, tableIds);
        Map<String, TableOccupancy> occupancyByTableId = activeOccupancies.stream()
                .collect(Collectors.toMap(o -> o.getTable().getTableId(), o -> o));

        List<Reservation> activeReservations = reservationRepository
                .findActiveReservations(date, time, tableIds);
        Map<String, Reservation> reservationByTableId = activeReservations.stream()
                .collect(Collectors.toMap(r -> r.getTable().getTableId(), r -> r));

        LocalTime futureTime = time.plusMinutes(UPCOMING_RESERVATION_WINDOW_MINUTES);
        List<Reservation> upcomingReservations = reservationRepository
                .findUpcomingReservations(date, time, futureTime, tableIds);
        Map<String, Reservation> upcomingReservationTimeByTableId = upcomingReservations.stream()
                .collect(Collectors.toMap(r -> r.getTable().getTableId(), r -> r));

        return allTables
                .map(table -> {
                    String tableId = table.getTableId();
                    TableOccupancy occupancy = occupancyByTableId.get(tableId);
                    if (occupancy != null) {
                        if (TableOccupancyStatus.CLEANING.equals(occupancy.getStatus())) {
                            return TableDTO.builder()
                                    .tableId(tableId)
                                    .tableNumber(table.getTableNumber())
                                    .location(table.getLocation())
                                    .capacity(table.getCapacity())
                                    .occupancyId(occupancy.getId())
                                    .status(TableStatusConstant.CLEANING)
                                    .build();
                        }else if (TableOccupancyStatus.SEATED.equals(occupancy.getStatus())) {
                            return TableDTO.builder()
                                    .tableId(tableId)
                                    .tableNumber(table.getTableNumber())
                                    .location(table.getLocation())
                                    .capacity(table.getCapacity())
                                    .occupiedAt(occupancy.getStartTime())
                                    .occupancyId(occupancy.getId())
                                    .status(TableStatusConstant.OCCUPIED)
                                    .build();
                        }
                    }

                    Reservation reservation = reservationByTableId.get(tableId);
                    if (reservation != null) {
                        return TableDTO.builder()
                                .tableId(tableId)
                                .tableNumber(table.getTableNumber())
                                .location(table.getLocation())
                                .capacity(table.getCapacity())
                                .reservationId(reservation.getReservationId())
                                .contactName(reservation.getCustomer().getContactName())
                                .phone(reservation.getCustomer().getProfile().getPhone())
                                .notes(reservation.getNotes())
                                .partySize(reservation.getNumberOfGuests())
                                .startAt(reservation.getStartTime())
                                .status(TableStatusConstant.RESERVED)
                                .build();
                    }

                    Reservation incomingReservation = upcomingReservationTimeByTableId.get(tableId);
                    if (incomingReservation != null) {
                        return TableDTO.builder()
                                .tableId(tableId)
                                .tableNumber(table.getTableNumber())
                                .location(table.getLocation())
                                .capacity(table.getCapacity())
                                .reservationId(incomingReservation.getReservationId())
                                .contactName(incomingReservation.getCustomer().getContactName())
                                .phone(incomingReservation.getCustomer().getProfile().getPhone())
                                .notes(incomingReservation.getNotes())
                                .partySize(incomingReservation.getNumberOfGuests())
                                .startAt(incomingReservation.getStartTime())
                                .status(TableStatusConstant.RESERVED)
                                .build();
                    }

                    return TableDTO.builder()
                            .tableId(tableId)
                            .tableNumber(table.getTableNumber())
                            .location(table.getLocation())
                            .capacity(table.getCapacity())
                            .status(TableStatusConstant.UNOCCUPIED)
                            .build();
                });

    }

    @Override
    @Transactional(readOnly = true)
    public TableSummaryDTO getTableStatusSummary(LocalDate date, LocalTime time) {
        List<TableEntity> allTables = tableRepository.findByIsDeletedFalse();
        List<String> tableIds = allTables.stream()
                .map(TableEntity::getTableId)
                .collect(Collectors.toList());

        List<TableOccupancy> activeOccupancies = tableOccupancyRepository
                .findActiveOccupancies(date, tableIds);
        Map<String, TableOccupancy> occupancyByTableId = activeOccupancies.stream()
                .collect(Collectors.toMap(o -> o.getTable().getTableId(), o -> o));

        List<Reservation> activeReservations = reservationRepository
                .findActiveReservations(date, time, tableIds);
        Map<String, Reservation> reservationByTableId = activeReservations.stream()
                .collect(Collectors.toMap(r -> r.getTable().getTableId(), r -> r));

        LocalTime futureTime = time.plusMinutes(UPCOMING_RESERVATION_WINDOW_MINUTES);
        List<Reservation> upcomingReservations = reservationRepository
                .findUpcomingReservations(date, time, futureTime, tableIds);
        Map<String, Reservation> upcomingReservationByTableId = upcomingReservations.stream()
                .collect(Collectors.toMap(r -> r.getTable().getTableId(), r -> r));

        long occupied = 0;
        long unoccupied = 0;
        long cleaning = 0;
        long reserved = 0;

        for (TableEntity table : allTables) {
            String tableId = table.getTableId();
            TableOccupancy occupancy = occupancyByTableId.get(tableId);

            if (occupancy != null) {
                if (TableOccupancyStatus.CLEANING.equals(occupancy.getStatus())) {
                    cleaning++;
                    continue;
                } else if (TableOccupancyStatus.SEATED.equals(occupancy.getStatus())) {
                    occupied++;
                    continue;
                }
            }

            if (reservationByTableId.containsKey(tableId) || upcomingReservationByTableId.containsKey(tableId)) {
                reserved++;
                continue;
            }

            unoccupied++;
        }

        return TableSummaryDTO.builder()
                .occupied(occupied)
                .unoccupied(unoccupied)
                .cleaning(cleaning)
                .reserved(reserved)
                .total((long) allTables.size())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TableEntity> getAvailableTables(LocalDate date, LocalTime time, int partySize, int durationMinutes) {
        List<TableEntity> allTables = tableRepository.findAll();
        Set<String> unavailableTableIds = new HashSet<>();

        LocalDateTime requestedStart = LocalDateTime.of(date, time);
        LocalDateTime requestedEnd = requestedStart.plusMinutes(durationMinutes);

        for (TableEntity table : allTables) {
            List<TableOccupancy> activeOccupancies = tableOccupancyRepository.findActiveOccupancy(table.getTableId(), date, time);
            if (!activeOccupancies.isEmpty()) {
                unavailableTableIds.add(table.getTableId());
                continue;
            }

            List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(
                    table.getTableId(), date, time, requestedEnd.toLocalTime());
            if (!overlappingReservations.isEmpty()) {
                unavailableTableIds.add(table.getTableId());
            }
        }

        return allTables.stream()
                .filter(table -> !unavailableTableIds.contains(table.getTableId()))
                .filter(table -> table.getCapacity() >= partySize)
                .collect(Collectors.toList());
    }
}