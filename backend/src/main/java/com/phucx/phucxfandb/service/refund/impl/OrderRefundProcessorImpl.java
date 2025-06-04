package com.phucx.phucxfandb.service.refund.impl;

import com.phucx.phucxfandb.config.RefundConfig;
import com.phucx.phucxfandb.dto.response.RefundPreviewDTO;
import com.phucx.phucxfandb.entity.Order;
import com.phucx.phucxfandb.entity.Payment;
import com.phucx.phucxfandb.enums.Currency;
import com.phucx.phucxfandb.enums.OrderStatus;
import com.phucx.phucxfandb.enums.PaymentStatus;
import com.phucx.phucxfandb.service.order.OrderReaderService;
import com.phucx.phucxfandb.service.refund.OrderRefundProcessorService;
import com.phucx.phucxfandb.service.refund.RefundUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumSet;

import static com.phucx.phucxfandb.utils.RefundUtils.isRefundable;

@Service
@RequiredArgsConstructor
public class OrderRefundProcessorImpl implements OrderRefundProcessorService {
    private final RefundUpdateService refundUpdateService;
    private final OrderReaderService orderReaderService;
    private final RefundConfig refundConfig;

    @Override
    public void processRefund(Order order) {
        Double percentage = refundConfig.getPercentages().getOrDefault(order.getStatus().name(), 0.0);
        BigDecimal refundAmount = calculateRefundAmount(order, percentage);
        switch (order.getType()){
            case DINE_IN -> handleDineInRefund(order, refundAmount);
            case TAKE_AWAY -> handleTakeAwayRefund(order, refundAmount);
        };
    }

    @Override
    public RefundPreviewDTO validateRefund(String orderId) {
        Order entity = orderReaderService.getOrderEntity(orderId);
        Double percentage = refundConfig.getPercentages().getOrDefault(entity.getStatus().name(), 0.0);

        var refundableStatuses = EnumSet.of(
                OrderStatus.PENDING,
                OrderStatus.CONFIRMED,
                OrderStatus.PREPARING,
                OrderStatus.PREPARED
        );

        String type = "ORDER";
        String status = entity.getStatus().name();
        Payment payment = entity.getPayment();

        if (payment == null) {
            return RefundPreviewDTO.builder()
                    .eligible(false)
                    .refundable(false)
                    .refundAmount(BigDecimal.ZERO)
                    .refundPercentage(0.0)
                    .currency(Currency.USD.name())
                    .paymentMethod("N/A")
                    .reason("No payment found for this order.")
                    .status(status)
                    .orderType(type)
                    .paymentStatus("UNPAID")
                    .paymentId(null)
                    .refundPolicyNote("Refund cannot be processed without payment.")
                    .build();
        }

        boolean refundableMethod = isRefundable(payment.getMethod());
        boolean eligibleStatus = refundableStatuses.contains(entity.getStatus());
        boolean eligiblePayment = PaymentStatus.SUCCESSFUL.equals(payment.getStatus());

        boolean eligible = refundableMethod && eligibleStatus && eligiblePayment;

        BigDecimal refundAmount = eligible
                ? entity.getTotalPrice().multiply(BigDecimal.valueOf(percentage)).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        String reason = eligible
                ? String.format("%.0f%% refund based on order status: %s", percentage * 100, status)
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

    private BigDecimal calculateRefundAmount(Order order, Double percentage) {
        return order.getTotalPrice().multiply(BigDecimal.valueOf(percentage));
    }

    private void handleTakeAwayRefund(Order order, BigDecimal refundAmount) {
        if (refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("No refund applicable for order status: " + order.getStatus());
        }
        refundUpdateService.createRefund(order.getPayment().getPaymentId(), refundAmount);
    }

    private void handleDineInRefund(Order order, BigDecimal refundAmount) {
        if (refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("No refund applicable for order status: " + order.getStatus());
        }
        refundUpdateService.createRefund(order.getPayment().getPaymentId(), refundAmount);
    }
}
