package com.phucx.phucxfoodshop.constant;

public enum NotificationTitle {
    // order
    PLACE_ORDER("Place Order"),
    CONFIRM_ORDER("Confirm Order"),
    RECEIVE_ORDER("Receive Order"),
    CANCEL_ORDER("Cancel Order"),
    FULFILL_ORDER("Fulfill Order"),
    ERROR_ORDER("Error order"),
    INVALID_ORDER("Invalid Order"),
    // account
    USER_INFO_UPDATE("User Infomation");

    private String value;
    private NotificationTitle(String value){
        this.value=value;
    }
    public String getValue(){
        return value;
    }
}
