package com.phucx.phucxfoodshop.constant;

public enum MarkNotificationType {
    NOTIFICATION,
    ALL,
    BROADCAST;

    public static MarkNotificationType fromString(String markType){
        for(MarkNotificationType type : MarkNotificationType.values()){
            if(type.name().equalsIgnoreCase(markType)){
                return type;
            }
        }
        throw new IllegalArgumentException("Mark type " + markType +" does not found");
    } 
}
