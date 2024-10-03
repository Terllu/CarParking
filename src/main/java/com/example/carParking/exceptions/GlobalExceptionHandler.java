package com.example.carParking.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            EntityNotFoundException.class,
            NoParkingFoundException.class,
            NoCarFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFoundException(RuntimeException ex) {
        logError(ex);
        ApiError apiError = buildApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({
            ParkingFullException.class,
            NoFreeSpaceParkingException.class,
            LpgNotAllowedException.class,
            CarTooWideException.class,
            NoFreeChargersParkingException.class,
            CarParkedException.class
    })
    public ResponseEntity<ApiError> handleBadRequestException(RuntimeException ex) {
        logWarning(ex);
        ApiError apiError = buildApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        logError(ex);
        ApiError apiError = buildApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private ApiError buildApiError(HttpStatus status, String message) {
        return ApiError.builder()
                .status(status)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private void logError(Exception ex) {
        logger.error("Error occurred: {}", ex.getMessage(), ex);
    }

    private void logWarning(Exception ex) {
        logger.warn("Warning: {}", ex.getMessage());
    }
}
