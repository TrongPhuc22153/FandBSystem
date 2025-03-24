package com.phucx.phucxfoodshop.exceptions;

public class EmailNotVerifiedException extends RuntimeException{

    public EmailNotVerifiedException(String message){
        super(message);
    }
}
