package com.deltadc.iot.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BaseExceptionResponse {

    private HttpStatus code;
    private String message;

    public BaseExceptionResponse(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}
