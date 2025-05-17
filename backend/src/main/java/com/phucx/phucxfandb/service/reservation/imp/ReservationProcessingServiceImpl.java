package com.phucx.phucxfandb.service.reservation.imp;

import com.phucx.phucxfandb.constant.*;
import com.phucx.phucxfandb.dto.request.RequestNotificationDTO;
import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.entity.ReservationTable;
import com.phucx.phucxfandb.service.notification.SendReservationNotificationService;
import com.phucx.phucxfandb.service.reservation.ReservationProcessingService;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import com.phucx.phucxfandb.service.reservation.ReservationUpdateService;
import com.phucx.phucxfandb.service.table.ReservationTableReaderService;
import com.phucx.phucxfandb.service.table.ReservationTableUpdateService;
import com.phucx.phucxfandb.utils.NotificationUtils;
import com.phucx.phucxfandb.utils.RoleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationProcessingServiceImpl implements ReservationProcessingService {
    private final ReservationReaderService reservationReaderService;
    private final ReservationUpdateService reservationUpdateService;
    private final ReservationTableUpdateService reservationTableUpdateService;
    private final ReservationTableReaderService reservationTableReaderService;
    private final SendReservationNotificationService sendReservationNotificationService;


    @Override
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
    public ReservationDTO placeReservation(RequestReservationDTO requestReservationDTO, Authentication authentication) {
        List<RoleName> roleNames = RoleUtils.getRoles(authentication.getAuthorities());
        ReservationDTO result;

        if(roleNames.contains(RoleName.CUSTOMER)){
            result = this.placeCustomerReservation(authentication.getName(), requestReservationDTO);
        }else if(roleNames.contains(RoleName.EMPLOYEE)){
            result = this.placeEmployeeReservation(authentication.getName(), requestReservationDTO);
        } else{
            throw new IllegalArgumentException("Invalid reservation");
        }

        sendReservationNotificationService.sendPlaceReservationNotification(
                authentication,
                result.getReservationId(),
                result
        );

        return result;
    }

    @Override
    public ReservationDTO placeCustomerReservation(String username, RequestReservationDTO requestReservationDTO) {
        ReservationTable table = reservationTableReaderService.getAvailableTable(
                requestReservationDTO.getNumberOfGuests(),
                requestReservationDTO.getStartTime(),
                requestReservationDTO.getEndTime()
        );
        requestReservationDTO.setTableId(table.getTableId());
        return reservationUpdateService.createCustomerReservation(username, requestReservationDTO);
    }

    @Override
    public ReservationDTO placeEmployeeReservation(String username, RequestReservationDTO requestReservationDTO) {
        ReservationTable table = reservationTableReaderService.getAvailableTable(
                requestReservationDTO.getNumberOfGuests(),
                requestReservationDTO.getStartTime(),
                requestReservationDTO.getEndTime()
        );
        requestReservationDTO.setTableId(table.getTableId());

        return reservationUpdateService
                .createEmployeeReservation(username, requestReservationDTO);
    }

    @Override
    public ReservationDTO completeReservation(String username, String reservationId) {
        ReservationDTO reservationDTO = reservationReaderService.getReservation(reservationId);
        reservationTableUpdateService.updateTableStatus(
                reservationDTO.getTable().getTableId(),
                TableStatus.UNOCCUPIED
        );
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
