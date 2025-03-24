package com.phucx.phucxfoodshop.exceptions;

import lombok.ToString;

@ToString
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){
        super(message);
    }
}
