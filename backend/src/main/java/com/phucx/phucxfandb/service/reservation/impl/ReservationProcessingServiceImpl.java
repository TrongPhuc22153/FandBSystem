package com.phucx.phucxfandb.service.reservation.impl;

import com.phucx.phucxfandb.constant.*;
import com.phucx.phucxfandb.dto.event.ReservationEvent;
import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.entity.*;
import com.phucx.phucxfandb.enums.*;
import com.phucx.phucxfandb.exception.TableNotAvailableException;
import com.phucx.phucxfandb.service.notification.SendReservationNotificationService;
import com.phucx.phucxfandb.service.refund.PayPalRefundService;
import com.phucx.phucxfandb.service.reservation.MenuItemService;
import com.phucx.phucxfandb.service.reservation.ReservationProcessingService;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import com.phucx.phucxfandb.service.reservation.ReservationUpdateService;
import com.phucx.phucxfandb.service.table.TableOccupancyUpdateService;
import com.phucx.phucxfandb.service.table.TableReaderService;
import com.phucx.phucxfandb.utils.NotificationUtils;
import com.phucx.phucxfandb.utils.RoleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.List;

import static com.phucx.phucxfandb.utils.RefundUtils.isAutoRefundable;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationProcessingServiceImpl implements ReservationProcessingService {
    private final ReservationUpdateService reservationUpdateService;
    private final ReservationReaderService reservationReaderService;
    private final PayPalRefundService payPalRefundService;
    private final TableReaderService tableReaderService;
    private final MenuItemService menuItemService;
    private final ApplicationEventPublisher eventPublisher;
    private final TableOccupancyUpdateService tableOccupancyUpdateService;
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
        validateReservation(reservationId);

        Reservation reservation = reservationReaderService.getReservationEntity(reservationId);
        EnumSet<ReservationStatus> cancellableStatuses = EnumSet.of(
                ReservationStatus.PENDING,
                ReservationStatus.CONFIRMED,
                ReservationStatus.PREPARING
        );

        if (!cancellableStatuses.contains(reservation.getStatus())) {
            throw new IllegalStateException("Reservation cannot be cancelled at this stage.");
        }

        Payment payment = reservation.getPayment();
        PaymentStatus paymentStatus = PaymentStatus.CANCELED;
        if (payment.getStatus() == PaymentStatus.SUCCESSFUL) {
            if (isAutoRefundable(payment.getMethod())) {
                payPalRefundService.refundPayment(payment.getPaymentId());
                paymentStatus = PaymentStatus.REFUNDED;
            }
        }

        ReservationDTO reservationDTO = reservationUpdateService.updateReservation(
                reservationId,
                ReservationStatus.CANCELED,
                paymentStatus
        );

        menuItemService.updateItemStatus(reservationId, MenuItemStatus.CANCELED);

        eventPublisher.publishEvent(ReservationEvent.builder()
                .id(reservationId)
                .action(EventAction.DELETE)
                .build());

        RequestNotificationDTO requestNotificationDTO = NotificationUtils.createRequestNotificationDTOForGroup(
                username,
                RoleName.EMPLOYEE,
                NotificationTopic.RESERVATION,
                NotificationTitle.RESERVATION_CANCELLED,
                NotificationMessage.RESERVATION_CANCELLED_MESSAGE
        );

        sendReservationNotificationService.sendNotificationToGroup(
                reservationDTO.getReservationId(),
                WebSocketEndpoint.TOPIC_EMPLOYEE,
                requestNotificationDTO
        );

        return reservationDTO;
    }

    private ReservationDTO cancelReservationByEmployee(String username, String reservationId) {
        validateReservation(reservationId);

        Reservation reservation = reservationReaderService.getReservationEntity(reservationId);
        EnumSet<ReservationStatus> cancellableStatuses = EnumSet.of(
                ReservationStatus.PENDING,
                ReservationStatus.CONFIRMED,
                ReservationStatus.PREPARING
        );

        if (!cancellableStatuses.contains(reservation.getStatus())) {
            throw new IllegalStateException("Reservation cannot be cancelled at this stage.");
        }

        Payment payment = reservation.getPayment();
        PaymentStatus paymentStatus = PaymentStatus.CANCELED;

        if (payment.getStatus() == PaymentStatus.SUCCESSFUL) {
            if (isAutoRefundable(payment.getMethod())) {
                payPalRefundService.refundPayment(payment.getPaymentId());
                paymentStatus = PaymentStatus.REFUNDED;
            }
        }

        ReservationDTO reservationDTO = reservationUpdateService.updateReservation(
                reservationId,
                ReservationStatus.CANCELED,
                paymentStatus
        );

        menuItemService.updateItemStatus(reservationId, MenuItemStatus.CANCELED);

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

    private void validateReservation(String reservationId){
        Reservation reservation = reservationReaderService.getReservationEntity(reservationId);
        if(!ReservationStatus.PENDING.equals(reservation.getStatus())){
            throw new IllegalStateException("Cannot cancel in process order");
        }

        for (MenuItem itemToCancel: reservation.getMenuItems()){
            if (!EnumSet.of(MenuItemStatus.PENDING, MenuItemStatus.PREPARING).contains(itemToCancel.getStatus())) {
                throw new IllegalStateException("Only items in PENDING or PREPARING status can be canceled.");
            }
        }
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
    @Transactional
    public ReservationDTO markReservationAsServed(Authentication authentication, String reservationId) {
        Reservation reservation = reservationReaderService.getReservationEntity(reservationId);
        var menuItemStatuses = EnumSet.of(MenuItemStatus.SERVED, MenuItemStatus.PREPARED, MenuItemStatus.CANCELED);
        for (MenuItem item : reservation.getMenuItems()) {
            if (!menuItemStatuses.contains(item.getStatus())) {
                throw new IllegalStateException("There are still in process food");
            }
        }
        if (!(ReservationStatus.READY_TO_SERVE.equals(reservation.getStatus()) ||
                ReservationStatus.PARTIALLY_SERVED.equals(reservation.getStatus()))) {
            throw new IllegalStateException("Reservation is not in a state that can be marked as served.");
        }

        menuItemService.updateItemStatus(reservationId, MenuItemStatus.SERVED);
        return reservationUpdateService.updateReservationStatus(reservationId, ReservationStatus.SERVED);
    }

    @Override
    @Transactional
    public ReservationDTO markReservationAsReady(Authentication authentication, String reservationId) {
        return reservationUpdateService.updateReservationStatus(reservationId, ReservationStatus.READY_TO_SERVE
        );
    }

    @Override
    public ReservationDTO placeCustomerReservation(String username, RequestReservationDTO request) {
        long durationMinutes = ChronoUnit.MINUTES.between(request.getStartTime(), request.getEndTime());
        if (durationMinutes < ReservationConstant.DEFAULT_DURATION_MINUTES) {
            throw new IllegalArgumentException("Reservation duration must be at least " + ReservationConstant.DEFAULT_DURATION_MINUTES + " minutes.");
        }

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
        if (durationMinutes < ReservationConstant.DEFAULT_DURATION_MINUTES) {
            throw new IllegalArgumentException("Reservation duration must be at least " + ReservationConstant.DEFAULT_DURATION_MINUTES + " minutes.");
        }

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
    @Transactional
    public ReservationDTO completeReservation(String username, String reservationId) {
        Reservation reservation = reservationReaderService.getReservationEntity(reservationId);
        if(!ReservationStatus.SERVED.equals(reservation.getStatus())){
            throw new IllegalStateException("Reservation is not served");
        }
        reservation.getMenuItems().forEach(item -> {
            if(!EnumSet.of(MenuItemStatus.SERVED, MenuItemStatus.CANCELED).contains(item.getStatus())){
                throw new IllegalStateException("There are still ongoing foods");
            }
        });

        tableOccupancyUpdateService.updateTableOccupancyStatus(
                reservation.getTableOccupancy().getId(),
                reservation.getTableOccupancy().getTable().getTableId(),
                TableOccupancyStatus.COMPLETED);

        return reservationUpdateService.updateReservationStatus(
                reservationId,
                ReservationStatus.COMPLETED
        );
    }

    @Override
    @Transactional
    public ReservationDTO preparingReservation(String username, String reservationId) {
        menuItemService.updateItemStatus(reservationId, MenuItemStatus.PENDING, MenuItemStatus.PREPARING);
        return reservationUpdateService.updateReservationStatus(reservationId, ReservationStatus.PREPARING);
    }

    @Override
    @Transactional
    public ReservationDTO markReservationAsPrepared(String username, String reservationId) {
        menuItemService.updateItemStatus(reservationId, MenuItemStatus.PREPARING, MenuItemStatus.PREPARED);
        return reservationUpdateService.updateReservationStatus(reservationId, ReservationStatus.PREPARED);
    }

    @Override
    public ReservationDTO processReservation(Authentication authentication, String reservationId, ReservationAction action) {
        ReservationDTO result = switch (action){
            case PREPARING -> this.preparingReservation(authentication.getName(), reservationId);
            case PREPARED -> this.markReservationAsPrepared(authentication.getName(), reservationId);
            case READY -> this.markReservationAsReady(authentication, reservationId);
            case SERVED -> this.markReservationAsServed(authentication, reservationId);
            case COMPLETE -> this.completeReservation(authentication.getName(), reservationId);
            case CANCEL -> this.cancelReservation(authentication, reservationId);
        };

        sendReservationNotificationService.sendNotificationForReservationAction(authentication, reservationId, action, result);

        return result;
    }
}
