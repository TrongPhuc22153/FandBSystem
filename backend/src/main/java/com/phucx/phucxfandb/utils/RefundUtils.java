package com.phucx.phucxfandb.utils;

import com.phucx.phucxfandb.constant.PaymentMethodConstants;
import com.phucx.phucxfandb.entity.PaymentMethod;

import static com.phucx.phucxfandb.constant.PaymentMethodConstants.*;

public class RefundUtils {
    public static boolean isRefundable(PaymentMethod method) {
        return switch (method.getMethodName().toLowerCase()) {
            case PAYPAL -> true;
            case CASH, COD -> false;
            default -> throw new IllegalStateException("Unexpected value: " + method.getMethodName().toLowerCase());
        };
    }

    public static boolean isAutoRefundable(PaymentMethod method) {
        return method.getMethodName().equalsIgnoreCase(PaymentMethodConstants.PAYPAL);
    }
}
