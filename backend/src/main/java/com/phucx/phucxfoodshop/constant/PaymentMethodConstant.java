package com.phucx.phucxfoodshop.constant;

public enum PaymentMethodConstant {
    PAYPAL("paypal"),
    COD("cod"),
    ZALOPAY("zalopay");

    private String value;
    private PaymentMethodConstant(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    public static PaymentMethodConstant fromString(String value) {
        for (PaymentMethodConstant paymentMethod : values()) {
            if (paymentMethod.getValue().equalsIgnoreCase(value)) {
                return paymentMethod;
            }
        }
        return null;
    }
}
