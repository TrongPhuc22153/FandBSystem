package com.phucx.phucxfandb.service.refund.impl;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpRequest;
import com.paypal.http.HttpResponse;
import com.paypal.payments.Money;
import com.paypal.payments.Refund;
import com.phucx.phucxfandb.config.RefundConfig;
import com.phucx.phucxfandb.dto.response.PayPalRefundDTO;
import com.phucx.phucxfandb.entity.Order;
import com.phucx.phucxfandb.entity.Payment;
import com.phucx.phucxfandb.entity.Reservation;
import com.phucx.phucxfandb.enums.Currency;
import com.phucx.phucxfandb.enums.PaymentStatus;
import com.phucx.phucxfandb.exception.NotFoundException;
import com.phucx.phucxfandb.repository.PaymentRepository;
import com.phucx.phucxfandb.service.refund.OrderRefundProcessorService;
import com.phucx.phucxfandb.service.refund.PayPalRefundService;
import com.phucx.phucxfandb.service.refund.ReservationRefundProcessorService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PayPalRefundServiceImpl implements PayPalRefundService {
    private final RefundConfig refundConfig;
    private final PaymentRepository paymentRepository;
    private final PayPalHttpClient payPalHttpClient;
    private final OrderRefundProcessorService orderRefundProcessorService;
    private final ReservationRefundProcessorService reservationRefundProcessorService;

    @Override
    @Transactional
    public PayPalRefundDTO refundPayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException(Payment.class.getSimpleName(), "id", paymentId));

        if (!PaymentStatus.SUCCESSFUL.equals(payment.getStatus())) {
            throw new IllegalStateException("Payment is not successful, cannot process refund: " + payment.getStatus());
        }

        Map<String, Double> refundPercentages = refundConfig.getPercentages();
        BigDecimal refundAmount = BigDecimal.ZERO;

        if (payment.getOrder() != null) {
            refundAmount = calculateRefundAmount(payment.getOrder(), refundPercentages);
        } else if (payment.getReservation() != null) {
            refundAmount = calculateRefundAmount(payment.getReservation(), refundPercentages);
        }

        if (refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Refund amount is zero — no refund applicable.");
        }

        return processPayPalRefund(payment.getPaypalCaptureId(), refundAmount);
    }

    private BigDecimal calculateRefundAmount(Order order, Map<String, Double> refundPercentages) {
        Double percentage = refundPercentages.getOrDefault(order.getStatus().name(), 0.0);
        orderRefundProcessorService.processRefund(order);
        return order.getTotalPrice().multiply(BigDecimal.valueOf(percentage));
    }

    private BigDecimal calculateRefundAmount(Reservation reservation, Map<String, Double> refundPercentages) {
        Double percentage = refundPercentages.getOrDefault(reservation.getStatus().name(), 0.0);
        reservationRefundProcessorService.processRefund(reservation);
        return reservation.getTotalPrice().multiply(BigDecimal.valueOf(percentage));
    }

    private PayPalRefundDTO processPayPalRefund(String paymentId, BigDecimal amount) {
        try {
            HttpRequest<Refund> request = getRefundHttpRequest(paymentId, amount);

            HttpResponse<Refund> response = payPalHttpClient.execute(request);

            return PayPalRefundDTO.builder()
                    .id(response.result().id())
                    .status(response.result().status())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("PayPal refund request failed", e);
        }
    }

    private static @NotNull HttpRequest<Refund> getRefundHttpRequest(String paymentId, BigDecimal amount) {
        Money money = new Money();
        money.currencyCode(Currency.USD.name());
        money.value(amount.setScale(2, RoundingMode.HALF_UP).toString());


        Refund refundRequest = new Refund();
        refundRequest.amount(money);

        HttpRequest<Refund> request = new HttpRequest<>(
                "/v2/payments/captures/" + paymentId + "/refund",
                "POST",
                Refund.class
        );
        request.header("Content-Type", "application/json");
        request.requestBody(refundRequest);
        return request;
    }
}
