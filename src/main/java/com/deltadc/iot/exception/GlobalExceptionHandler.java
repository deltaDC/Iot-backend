package com.deltadc.iot.exception;

import com.deltadc.iot.response.BaseExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleException(IllegalArgumentException e) {
        log.debug(e.getLocalizedMessage(), e);

        BaseExceptionResponse baseExceptionResponse = new BaseExceptionResponse(
                HttpStatus.NOT_ACCEPTABLE,
                e.getLocalizedMessage()
        );

        return ResponseEntity.status(500)
                .body(baseExceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        log.debug(e.getLocalizedMessage(), e);

        BaseExceptionResponse baseExceptionResponse = new BaseExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later."
        );

        return ResponseEntity.status(500)
                .body(baseExceptionResponse);
    }
}
