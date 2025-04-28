package com.phucx.phucxfandb.exception;

public class ReservationNotAvailableException extends RuntimeException{
    public ReservationNotAvailableException(String message){
        super(message);
    }

    public ReservationNotAvailableException(String resource, String name){
        super(String.format("%s %s is occupied", resource, name));
    }
}
