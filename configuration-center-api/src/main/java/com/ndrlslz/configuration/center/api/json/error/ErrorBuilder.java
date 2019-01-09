package com.ndrlslz.configuration.center.api.json.error;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public class ErrorBuilder {
    public static Error badRequest(Throwable exception) {
        return newError(exception, BAD_REQUEST);
    }

    public static Error internalServerError(Throwable exception) {
        return newError(exception, INTERNAL_SERVER_ERROR);
    }

    public static Error conflict(Throwable exception) {
        return newError(exception, CONFLICT);
    }

    public static Error notFound(Throwable exception) {
        return newError(exception, NOT_FOUND);
    }
    private static Error newError(Throwable exception, HttpStatus httpStatus) {
        Error error = new Error();
        error.setException(exception.getClass().getName());
        error.setMessage(exception.getMessage());
        error.setStatus(httpStatus.value());
        error.setError(httpStatus.getReasonPhrase());

        return error;
    }
}