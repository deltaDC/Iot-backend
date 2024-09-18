package com.deltadc.iot.exception;

import com.deltadc.iot.response.BaseExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InterruptedException.class, ExecutionException.class})
    public ResponseEntity<Object> handleMqttExceptions(Exception e) {
        log.error("Failed to receive LED status", e);

        BaseExceptionResponse baseExceptionResponse = new BaseExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Failed to receive LED status"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(baseExceptionResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleException(IllegalArgumentException e) {
        log.debug(e.getLocalizedMessage(), e);

        BaseExceptionResponse baseExceptionResponse = new BaseExceptionResponse(
                HttpStatus.NOT_ACCEPTABLE,
                e.getLocalizedMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(baseExceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        log.debug(e.getLocalizedMessage(), e);

        BaseExceptionResponse baseExceptionResponse = new BaseExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later."
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(baseExceptionResponse);
    }
}
