package com.phucx.phucxfandb.service.refund.impl;

import com.phucx.phucxfandb.config.RefundConfig;
import com.phucx.phucxfandb.constant.PaymentMethodTypeConstant;
import com.phucx.phucxfandb.dto.response.RefundPreviewDTO;
import com.phucx.phucxfandb.entity.Payment;
import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.enums.Currency;
import com.phucx.phucxfandb.enums.PaymentStatus;
import com.phucx.phucxfandb.enums.ReservationStatus;
import com.phucx.phucxfandb.service.refund.RefundUpdateService;
import com.phucx.phucxfandb.service.refund.ReservationRefundProcessorService;
import com.phucx.phucxfandb.service.reservation.ReservationReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumSet;

import static com.phucx.phucxfandb.utils.RefundUtils.isRefundable;

@Service
@RequiredArgsConstructor
public class ReservationRefundProcessorImpl implements ReservationRefundProcessorService {
    private final ReservationReaderService reservationReaderService;
    private final RefundUpdateService refundUpdateService;
    private final RefundConfig refundConfig;

    @Override
    public void processRefund(Reservation reservation) {
        Double percentage = refundConfig.getPercentages().getOrDefault(reservation.getStatus().name(), 0.0);
        BigDecimal refundAmount = calculateRefundAmount(reservation, percentage);
        refundUpdateService.createRefund(reservation.getPayment().getPaymentId(), refundAmount);
    }

    @Override
    public RefundPreviewDTO validateRefund(String reservationId) {
        var refundableStatuses = EnumSet.of(
                ReservationStatus.PENDING,
                ReservationStatus.CONFIRMED,
                ReservationStatus.PREPARING,
                ReservationStatus.PREPARED
        );

        Reservation reservation = reservationReaderService.getReservationEntity(reservationId);
        Double percentage = refundConfig.getPercentages().getOrDefault(reservation.getStatus().name(), 0.0);

        String type = PaymentMethodTypeConstant.RESERVATION.toUpperCase();
        String status = reservation.getStatus().name();
        Payment payment = reservation.getPayment();

        if (payment == null) {
            return RefundPreviewDTO.builder()
                    .eligible(false)
                    .refundable(false)
                    .refundAmount(BigDecimal.ZERO)
                    .refundPercentage(0.0)
                    .currency(Currency.USD.name())
                    .paymentMethod("N/A")
                    .reason("No payment found for this reservation.")
                    .status(status)
                    .orderType(type)
                    .paymentStatus("UNPAID")
                    .paymentId(null)
                    .refundPolicyNote("Refund cannot be processed without payment.")
                    .build();
        }

        boolean refundableMethod = isRefundable(payment.getMethod());
        boolean eligibleStatus = refundableStatuses.contains(reservation.getStatus());
        boolean eligiblePayment = PaymentStatus.SUCCESSFUL.equals(payment.getStatus());

        boolean eligible = refundableMethod && eligibleStatus && eligiblePayment;

        BigDecimal refundAmount = eligible
                ? reservation.getTotalPrice().multiply(BigDecimal.valueOf(percentage)).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        String reason = eligible
                ? String.format("%.0f%% refund based on reservation status: %s", percentage * 100, status)
                : "Refund not applicable due to status, payment method, or payment status.";

        return RefundPreviewDTO.builder()
                .eligible(eligible)
                .refundable(refundableMethod)
                .refundAmount(refundAmount)
                .refundPercentage(percentage)
                .currency(Currency.USD.name())
                .paymentMethod(payment.getMethod().getMethodName())
                .reason(reason)
                .status(status)
                .orderType(type)
                .paymentStatus(payment.getStatus().name())
                .paymentId(payment.getPaymentId())
                .refundPolicyNote("Full refund is only available before preparation starts.")
                .build();
    }

    private BigDecimal calculateRefundAmount(Reservation reservation, Double percentage) {
        return reservation.getTotalPrice().multiply(BigDecimal.valueOf(percentage));
    }
}
