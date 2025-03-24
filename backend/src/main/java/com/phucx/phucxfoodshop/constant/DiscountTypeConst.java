package com.phucx.phucxfoodshop.constant;

public enum DiscountTypeConst {
    Code("code"),
    Percentage_based("percentage-based");

    private String value;
    DiscountTypeConst(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }
}
