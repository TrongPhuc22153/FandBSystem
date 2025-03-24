package com.phucx.phucxfoodshop.constant;

public enum OrderStatus {
    Pending,
    Confirmed,
    Shipping,
    Successful,
    Canceled,
    All;

    public static OrderStatus fromString(String status){
        for(OrderStatus orderStatus : OrderStatus.values()){
            if(orderStatus.name().equalsIgnoreCase(status)){
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("Status " + status +" does not found");
    } 
}
