package com.phucx.phucxfandb.service.reservation.imp;

import com.phucx.phucxfandb.constant.ReservationStatus;
import com.phucx.phucxfandb.constant.TableStatus;
import com.phucx.phucxfandb.dto.request.RequestReservationDTO;
import com.phucx.phucxfandb.dto.response.ReservationDTO;
import com.phucx.phucxfandb.entity.ReservationTable;
import com.phucx.phucxfandb.service.notification.SendReservationNotificationService;
import com.phucx.phucxfandb.service.reservation.ReservationProcessingService;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import com.phucx.phucxfandb.service.reservation.ReservationUpdateService;
import com.phucx.phucxfandb.service.table.ReservationTableReaderService;
import com.phucx.phucxfandb.service.table.ReservationTableUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public ReservationDTO confirmReservation(String username, String reservationId) {
        log.info("confirmReservation(username={}, reservationId={})", username, reservationId);
        return reservationUpdateService.updateReservationStatus(reservationId, ReservationStatus.CONFIRMED);
    }

    @Override
    public void cancelReservation(String username, String reservationId) {
        log.info("cancelReservation(username={}, reservationId={})", username, reservationId);
        reservationUpdateService.updateReservationStatus(reservationId, ReservationStatus.CANCELED);
    }

    @Override
    public ReservationDTO markReservationAsReceived(String username, String reservationId) {
        log.info("markReservationAsReceived(username={}, reservationId={})", username, reservationId);
        ReservationDTO reservationDTO = reservationReaderService.getReservation(reservationId);
        reservationTableUpdateService.updateTableStatus(
                reservationDTO.getTable().getTableId(),
                TableStatus.OCCUPIED
        );
        return reservationUpdateService.updateReservationStatus(
                reservationDTO.getReservationId(),
                ReservationStatus.SEATED);
    }

    @Override
    public ReservationDTO placeCustomerReservation(String username, RequestReservationDTO requestReservationDTO) {
        log.info("placeCustomerReservation(username={}, requestReservationDTO={})", username, requestReservationDTO);
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
        log.info("placeEmployeeReservation(username={}, requestReservationDTO={})", username, requestReservationDTO);
        ReservationTable table = reservationTableReaderService.getAvailableTable(
                requestReservationDTO.getNumberOfGuests(),
                requestReservationDTO.getStartTime(),
                requestReservationDTO.getEndTime()
        );
        requestReservationDTO.setTableId(table.getTableId());
        return reservationUpdateService.createEmployeeReservation(username, requestReservationDTO);
    }

    @Override
    public ReservationDTO completeReservation(String username, String reservationId) {
        log.info("completeReservation(username={}, reservationId={})", username, reservationId);
        ReservationDTO reservationDTO = reservationReaderService.getReservation(reservationId);
        reservationTableUpdateService.updateTableStatus(
                reservationDTO.getTable().getTableId(),
                TableStatus.UNOCCUPIED
        );
        return reservationUpdateService.updateReservationStatus(
                reservationId,
                ReservationStatus.COMPLETE
        );
    }

    @Override
    public ReservationDTO markReservationAsPrepared(String reservationId) {
        log.info("markReservationAsPrepared(reservationId={})", reservationId);
        return reservationUpdateService.updateReservationStatus(reservationId, ReservationStatus.PREPARED);
    }
}
