package com.example.postapp.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class ApiException {

    private final String message;
    private final HttpStatus httpStatus;
    private final Date date;

    public ApiException(String message, HttpStatus httpStatus, Date date) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.date = date;
    }

}
