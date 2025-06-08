package com.phucx.phucxfandb.utils;

import com.phucx.phucxfandb.constant.PaymentMethodConstants;
import com.phucx.phucxfandb.entity.Payment;
import com.phucx.phucxfandb.enums.OrderType;
import com.phucx.phucxfandb.enums.PaymentStatus;

public class CartUtils {
    public static boolean isShouldRemoveCart(Payment payment, OrderType orderType) {
        String method = payment.getMethod().getMethodName();
        PaymentStatus status = payment.getStatus();
        boolean shouldRemoveCart = false;

        if (PaymentMethodConstants.PAYPAL.equalsIgnoreCase(method) && status == PaymentStatus.SUCCESSFUL) {
            shouldRemoveCart = true;
        } else if (PaymentMethodConstants.COD.equalsIgnoreCase(method)
                && (status == PaymentStatus.PENDING || status == PaymentStatus.SUCCESSFUL)
                && orderType == OrderType.TAKE_AWAY) {
            shouldRemoveCart = true;
        } else if (PaymentMethodConstants.CASH.equalsIgnoreCase(method)
                && status == PaymentStatus.SUCCESSFUL
                && orderType == OrderType.DINE_IN) {
            shouldRemoveCart = true;
        }
        return shouldRemoveCart;
    }
}
