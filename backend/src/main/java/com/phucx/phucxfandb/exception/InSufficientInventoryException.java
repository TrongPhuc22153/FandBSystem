package com.phucx.phucxfandb.exception;

public class InSufficientInventoryException extends RuntimeException {
    public InSufficientInventoryException(String message){
        super(message);
    }
}
