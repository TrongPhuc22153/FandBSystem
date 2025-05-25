package com.phucx.phucxfandb.exception;

import com.phucx.phucxfandb.dto.response.ResponseDTO;
import com.phucx.phucxfandb.dto.response.ValidatedTokenResponseDTO;
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
        log.error("handleMethodArgumentNotValid({})", ex.getMessage());
        Map<String, List<String>> fieldErrors = new HashMap<>();
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

    @ExceptionHandler(value = TableException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleTableException(TableException exception) {
        log.error("handleTableException: {}", exception.getMessage(), exception);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error(exception.getMessage())
                .build();
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(value = ImageException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleImageException(ImageException exception) {
        log.error("handleImageException: {}", exception.getMessage(), exception);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error(exception.getMessage())
                .build();
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(value = PaymentException.class)
    protected ResponseEntity<ResponseDTO<Void>> handlePaymentException(PaymentException exception) {
        log.error("handlePaymentException: {}", exception.getMessage(), exception);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error("PAYMENT_EXCEPTION")
                .build();
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error("handleIllegalArgumentException: {}", exception.getMessage(), exception);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error("ILLEGAL_ARGUMENT")
                .build();
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleNotFoundException(NotFoundException exception) {
        log.error("handleNotFoundException: {}", exception.getMessage(), exception);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error("NOT_FOUND")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleIllegalStateException(IllegalStateException exception) {
        log.error("handleIllegalStateException: {}", exception.getMessage(), exception);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error("ILLEGAL_STATE")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(EntityExistsException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleEntityExistsException(EntityExistsException exception) {
        log.error("handleEntityExistsException: {}", exception.getMessage(), exception);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error("ENTITY_EXISTED")
                .build();
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(InsufficientResourcesException.class)
    protected ResponseEntity<ResponseDTO<Void>> handleInsufficientResourcesException(InsufficientResourcesException exception) {
        log.error("handleInsufficientResourcesException: {}", exception.getMessage(), exception);
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
        log.error("handleIOException: {}", exception.getMessage(), exception);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .message(exception.getMessage())
                .error("IO_EXCEPTION")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(InvalidTokenException.class)
    protected ResponseEntity<ResponseDTO<ValidatedTokenResponseDTO>> handleInvalidTokenException(InvalidTokenException exception) {
        log.error("handleInvalidTokenException: {}", exception.getMessage(), exception);
        ValidatedTokenResponseDTO validatedTokenResponseDTO = ValidatedTokenResponseDTO.builder()
                .status(Boolean.FALSE)
                .build();

        ResponseDTO<ValidatedTokenResponseDTO> response = ResponseDTO.<ValidatedTokenResponseDTO>builder()
                .message(exception.getMessage())
                .error("INVALID_TOKEN")
                .data(validatedTokenResponseDTO)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
