package com.phucx.phucxfoodshop.constant;

public enum JwtType {
    VERIFY_EMAIL("verify_email"),
    RESET_PASSWORD("reset_password");

    private String value;
    private JwtType(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }

}
