package com.phucx.phucxfandb.exception;

public class EntityExistsException extends RuntimeException{
    public EntityExistsException(String message){
        super(message);
    }
}
