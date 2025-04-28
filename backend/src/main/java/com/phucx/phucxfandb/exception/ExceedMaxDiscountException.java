package com.phucx.phucxfandb.exception;

public class ExceedMaxDiscountException extends RuntimeException{
    public ExceedMaxDiscountException(int productId){
        super(String.format("Product %d exceeds maximum number of discounts!", productId));
    }
}
