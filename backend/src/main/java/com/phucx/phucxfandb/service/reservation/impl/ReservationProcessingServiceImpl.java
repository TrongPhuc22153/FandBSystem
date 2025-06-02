package com.phucx.phucxfandb.service.reservation.impl;

import com.phucx.phucxfandb.constant.*;
import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.entity.TableEntity;
import com.phucx.phucxfandb.enums.*;
import com.phucx.phucxfandb.exception.TableNotAvailableException;
import com.phucx.phucxfandb.service.notification.SendReservationNotificationService;
import com.phucx.phucxfandb.service.reservation.ReservationProcessingService;
import com.phucx.phucxfandb.service.reservation.ReservationUpdateService;
import com.phucx.phucxfandb.service.table.TableReaderService;
import com.phucx.phucxfandb.utils.NotificationUtils;
import com.phucx.phucxfandb.utils.RoleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationProcessingServiceImpl implements ReservationProcessingService {
    private final ReservationUpdateService reservationUpdateService;
    private final TableReaderService tableReaderService;
    private final SendReservationNotificationService sendReservationNotificationService;

    @Override
    @Transactional
    public ReservationDTO cancelReservation(Authentication authentication, String reservationId) {
        List<RoleName> roleNames = RoleUtils.getRoles(authentication.getAuthorities());
        if (roleNames.contains(RoleName.CUSTOMER)) {
            return this.cancelReservationByCustomer(authentication.getName(), reservationId);
        } else if (roleNames.contains(RoleName.EMPLOYEE)) {
            return this.cancelReservationByEmployee(authentication.getName(), reservationId);
        } else {
            throw new IllegalArgumentException("Invalid role for cancelling reservation");
        }
    }

    private ReservationDTO cancelReservationByCustomer(String username, String reservationId) {
        ReservationDTO reservationDTO = reservationUpdateService.updateReservationStatusByCustomer(username, reservationId, ReservationStatus.CANCELLED);

        RequestNotificationDTO requestNotificationDTO = NotificationUtils.createRequestNotificationDTO(
                username,
                reservationDTO.getEmployee().getProfile().getUser().getUsername(),
                NotificationTopic.RESERVATION,
                NotificationTitle.RESERVATION_CANCELLED,
                NotificationMessage.RESERVATION_CANCELLED_MESSAGE
        );

        sendReservationNotificationService.sendNotificationToUser(
                reservationDTO.getReservationId(),
                requestNotificationDTO
        );

        return reservationDTO;
    }

    private ReservationDTO cancelReservationByEmployee(String username, String reservationId) {
        ReservationDTO reservationDTO = reservationUpdateService.updateReservationStatusByEmployee(username, reservationId, ReservationStatus.CANCELLED);

        RequestNotificationDTO requestNotificationDTO = NotificationUtils.createRequestNotificationDTO(
                username,
                reservationDTO.getCustomer().getProfile().getUser().getUsername(),
                NotificationTopic.RESERVATION,
                NotificationTitle.RESERVATION_CANCELLED,
                NotificationMessage.RESERVATION_CANCELLED_MESSAGE
        );

        sendReservationNotificationService.sendNotificationToUser(
                reservationDTO.getReservationId(),
                requestNotificationDTO
        );

        return reservationDTO;
    }

    @Override
    @Transactional
    public ReservationDTO placeReservation(RequestReservationDTO request, Authentication authentication) {

        if (request.getDate().isEqual(LocalDate.now()) && request.getStartTime().isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("Reservation time cannot be in the past");
        }
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        List<RoleName> roleNames = RoleUtils.getRoles(authentication.getAuthorities());
        ReservationDTO newReservation;

        if(roleNames.contains(RoleName.CUSTOMER)){
            newReservation = this.placeCustomerReservation(authentication.getName(), request);
        }else if(roleNames.contains(RoleName.EMPLOYEE)){
            newReservation = this.placeEmployeeReservation(authentication.getName(), request);
        } else{
            throw new IllegalArgumentException("Invalid reservation");
        }
        return newReservation;
    }

    @Override
    public ReservationDTO placeCustomerReservation(String username, RequestReservationDTO request) {
        long durationMinutes = ChronoUnit.MINUTES.between(request.getStartTime(), request.getEndTime());

        TableEntity table;
        List<TableEntity> availableTables = tableReaderService.getAvailableTables(
                request.getDate(), request.getStartTime(), request.getNumberOfGuests(), (int) durationMinutes);
        if (request.getTableId() != null) {
            table = availableTables.stream()
                    .filter(t -> t.getTableId().equals(request.getTableId()))
                    .findFirst()
                    .orElseThrow(() -> new TableNotAvailableException("Specified table is not available"));
        } else {
            if (availableTables.isEmpty()) {
                throw new TableNotAvailableException("No suitable table available for the requested time and party size");
            }
            table = availableTables.get(0);
        }

        request.setTableId(table.getTableId());
        return reservationUpdateService.createCustomerReservation(username, request);
    }

    @Override
    public ReservationDTO placeEmployeeReservation(String username, RequestReservationDTO request) {
        long durationMinutes = ChronoUnit.MINUTES.between(request.getStartTime(), request.getEndTime());

        TableEntity table;
        List<TableEntity> availableTables = tableReaderService.getAvailableTables(
                request.getDate(), request.getStartTime(), request.getNumberOfGuests(), (int) durationMinutes);
        if (request.getTableId() != null) {
            table = availableTables.stream()
                    .filter(t -> t.getTableId().equals(request.getTableId()))
                    .findFirst()
                    .orElseThrow(() -> new TableNotAvailableException("Specified table is not available"));
        } else {
            if (availableTables.isEmpty()) {
                throw new TableNotAvailableException("No suitable table available for the requested time and party size");
            }
            table = availableTables.get(0);
        }

        request.setTableId(table.getTableId());

        return reservationUpdateService.createEmployeeReservation(username, request);
    }

    @Override
    public ReservationDTO completeReservation(String username, String reservationId) {
        return reservationUpdateService.updateReservationStatus(
                reservationId,
                ReservationStatus.COMPLETED
        );
    }

    @Override
    public ReservationDTO preparingReservation(String username, String reservationId) {
        return reservationUpdateService.updateReservationStatus(reservationId, ReservationStatus.PREPARING);
    }

    @Override
    public ReservationDTO markReservationAsPrepared(String username, String reservationId) {
        return reservationUpdateService.updateReservationStatus(reservationId, ReservationStatus.PREPARED);
    }

    @Override
    public ReservationDTO processReservation(Authentication authentication, String reservationId, ReservationAction action) {
        ReservationDTO result = switch (action){
            case PREPARING -> this.preparingReservation(authentication.getName(), reservationId);
            case READY -> this.markReservationAsPrepared(authentication.getName(), reservationId);
            case COMPLETE -> this.completeReservation(authentication.getName(), reservationId);
            case CANCEL -> this.cancelReservation(authentication, reservationId);
        };

        sendReservationNotificationService.sendNotificationForReservationAction(authentication, reservationId, action, result);

        return result;
    }
}
