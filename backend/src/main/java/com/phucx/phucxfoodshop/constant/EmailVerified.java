package com.phucx.phucxfoodshop.constant;

public enum EmailVerified {
    YES(true),
    NO(false);

    private Boolean value;
    private EmailVerified(Boolean value){
        this.value=value;
    }

    public Boolean getValue(){
        return this.value;
    }
}
