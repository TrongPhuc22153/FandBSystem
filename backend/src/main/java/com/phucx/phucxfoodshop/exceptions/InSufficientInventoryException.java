package com.phucx.phucxfoodshop.exceptions;

public class InSufficientInventoryException extends Exception {
    public InSufficientInventoryException(String message){
        super(message);
    }
}
