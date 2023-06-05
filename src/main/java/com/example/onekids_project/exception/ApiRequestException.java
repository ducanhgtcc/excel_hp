package com.example.onekids_project.exception;

import org.springframework.http.HttpStatus;

public class ApiRequestException extends Exception {

    private static final long serialVersionUID = 5673885768780506185L;

    public HttpStatus httpStatus;

    // khoi tao contrustor
    public ApiRequestException(String message) {

        super(message);
    }

    public ApiRequestException(String message, HttpStatus httpStatus) {

        super(message);
        this.httpStatus = httpStatus;
    }

    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
