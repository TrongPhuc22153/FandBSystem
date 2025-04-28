package com.phucx.phucxfandb.constant;

public enum DiscountActive {
    ACTIVE(true),
    DEACTIVE(false);
    private boolean value;
    DiscountActive(boolean value){
        this.value=value;
    }
    public boolean getValue(){
        return this.value;
    }
}
