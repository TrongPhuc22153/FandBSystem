package com.phucx.phucxfandb.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String resourceName, String id){
        super(String.format("%s with id %s not found", resourceName, id));
    }
    public NotFoundException(String resourceName, String name, String value){
        super(String.format("%s with %s %s not found", resourceName, name, value));
    }
    public NotFoundException(String resourceName, String name, Long value){
        super(String.format("%s with %s %d not found", resourceName, name, value));
    }

    public NotFoundException(String resourceName, long id){
        super(String.format("%s with id %d not found", resourceName, id));
    }

    public NotFoundException(String resource1, String resource2, String name, String value){
        super(String.format("%s of %s with %s %s not found", resource1, resource2, name, value));
    }

    public NotFoundException(String message){
        super(message);
    }
}
