package com.phucx.phucxfoodshop.exceptions;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String message){
        super(message);
    }
}
