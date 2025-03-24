package com.phucx.phucxfoodshop.constant;

public enum UserStatus {
    ENABLED(true),
    DISABLED(false);
    private Boolean value;
    private UserStatus(Boolean value){
        this.value = value;
    }
    public Boolean getValue(){
        return this.value;
    }
}
