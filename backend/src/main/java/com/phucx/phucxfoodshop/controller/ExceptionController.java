package com.phucx.phucxfoodshop.controller;

import java.io.IOException;

import javax.naming.InsufficientResourcesException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.phucx.phucxfoodshop.exceptions.EmailNotVerifiedException;
import com.phucx.phucxfoodshop.exceptions.EmptyCartException;
import com.phucx.phucxfoodshop.exceptions.EntityExistsException;
import com.phucx.phucxfoodshop.exceptions.InSufficientInventoryException;
import com.phucx.phucxfoodshop.exceptions.InvalidDiscountException;
import com.phucx.phucxfoodshop.exceptions.InvalidOrderException;
import com.phucx.phucxfoodshop.exceptions.InvalidTokenException;
import com.phucx.phucxfoodshop.exceptions.NotFoundException;
import com.phucx.phucxfoodshop.exceptions.UserAuthenticationException;
import com.phucx.phucxfoodshop.exceptions.UserPasswordException;
import com.phucx.phucxfoodshop.model.ResponseFormat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = EmailNotVerifiedException.class)
    protected ResponseEntity<ResponseFormat> handleEmailNotVerified(EmailNotVerifiedException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = InvalidTokenException.class)
    protected ResponseEntity<ResponseFormat> handleInvalidTokenException(InvalidTokenException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = UserPasswordException.class)
    protected ResponseEntity<ResponseFormat> handleUserPasswordException(UserPasswordException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = UserAuthenticationException.class)
    protected ResponseEntity<ResponseFormat> handleAuthenticationException(UserAuthenticationException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = InvalidDiscountException.class)
    protected ResponseEntity<ResponseFormat> handleInvalidDiscountException(InvalidDiscountException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = InSufficientInventoryException.class)
    protected ResponseEntity<ResponseFormat> handleInSufficientInventoryException(InSufficientInventoryException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<ResponseFormat> handleIllegalArgumentException(IllegalArgumentException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = NullPointerException.class)
    protected ResponseEntity<ResponseFormat> handleNullPointerException(NullPointerException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setStatus(false);
        response.setError(exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EmptyCartException.class)
    protected ResponseEntity<ResponseFormat> handleEmptyCartException(EmptyCartException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setError(exception.getMessage());
        response.setStatus(false);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @ExceptionHandler(InvalidOrderException.class)
    protected ResponseEntity<ResponseFormat> handleInvalidOrderException(InvalidOrderException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setError(exception.getMessage());
        response.setStatus(false);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ResponseFormat> handleRuntimeException(RuntimeException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setError(exception.getMessage());
        response.setStatus(false);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ResponseFormat> handlerNotFoundException(NotFoundException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setError(exception.getMessage());
        response.setStatus(false);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(EntityExistsException.class)
    protected ResponseEntity<ResponseFormat> handlerEntityExistsException(EntityExistsException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setError(exception.getMessage());
        response.setStatus(false);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @ExceptionHandler(InsufficientResourcesException.class)
    protected ResponseEntity<ResponseFormat> handlerInsufficientResourcesException(InsufficientResourcesException exception){
        log.error("Error: {}", exception.getMessage());
        ResponseFormat response = new ResponseFormat();
        response.setError(exception.getMessage());
        response.setStatus(false);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<Void> handlerIOExceptionr(IOException exception){
        log.error("Error: {}", exception.getMessage());
        return ResponseEntity.notFound().build();
    }
}
