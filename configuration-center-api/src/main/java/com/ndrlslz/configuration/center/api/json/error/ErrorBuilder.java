package com.ndrlslz.configuration.center.api.json.error;

import org.springframework.http.HttpStatus;

public class ErrorBuilder {
    public static Error badRequest(Exception exception) {
        return newError(exception, HttpStatus.BAD_REQUEST);
    }

    private static Error newError(Exception exception, HttpStatus httpStatus) {
        Error error = new Error();
        error.setException(exception.getClass().getName());
        error.setMessage(exception.getMessage());
        error.setStatus(httpStatus.value());
        error.setError(httpStatus.getReasonPhrase());

        return error;
    }
}