package com.phucx.phucxfandb.service.refund;

import com.phucx.phucxfandb.dto.response.RefundPreviewDTO;
import com.phucx.phucxfandb.entity.Reservation;

public interface ReservationRefundProcessorService extends RefundProcessorService<Reservation> {
    RefundPreviewDTO validateRefund(String reservationId);
}
