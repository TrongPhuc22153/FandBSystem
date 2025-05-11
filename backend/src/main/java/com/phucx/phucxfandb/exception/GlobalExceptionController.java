package com.phucx.phucxfandb.exception;

import com.phucx.phucxfandb.dto.response.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.InsufficientResourcesException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("handleMethodArgumentNotValid({})", ex.getMessage());
        Map<String, List<String>> fieldErrors = new HashMap<>();
        // Group all error messages by field to support multiple errors per field
        for (FieldError error : ex.getFieldErrors()) {
            fieldErrors.computeIfAbsent(error.getField(), k -> new ArrayList<>())
                    .add(error.getDefaultMessage());
        }
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .error("INVALID_FIELDS")
                .fields(fieldErrors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = EmailNotVerifiedException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleEmailNotVerified(EmailNotVerifiedException exception) {
        log.error("handleEmailNotVerified: {}", exception.getMessage());
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error("Email not verified")
                .build();
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    @ExceptionHandler(value = InSufficientInventoryException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleInSufficientInventoryException(InSufficientInventoryException exception) {
        log.error("handleInSufficientInventoryException: {}", exception.getMessage());
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error("Insufficient inventory")
                .build();
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error("handleIllegalArgumentException: {}", exception.getMessage());
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error("Illegal argument")
                .build();
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(value = NullPointerException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleNullPointerException(NullPointerException exception) {
        log.error("handleNullPointerException: {}", exception.getMessage());
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error("Null pointer")
                .build();
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(EmptyCartException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleEmptyCartException(EmptyCartException exception) {
        log.error("handleEmptyCartException: {}", exception.getMessage());
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error("Empty cart")
                .build();
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleNotFoundException(NotFoundException exception) {
        log.error("handleNotFoundException ", exception);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error("Resource not found")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(EntityExistsException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleEntityExistsException(EntityExistsException exception) {
        log.error("handleEntityExistsException: {}", exception.getMessage());
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error("Entity already exists")
                .build();
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(InsufficientResourcesException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleInsufficientResourcesException(InsufficientResourcesException exception) {
        log.error("handleInsufficientResourcesException: {}", exception.getMessage());
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error("Insufficient resources")
                .build();
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleIOException(IOException exception) {
        log.error("handleIOException: {}", exception.getMessage());
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error("IO error")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
