package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.api.exception.InvalidRequestBodyException;
import com.ndrlslz.configuration.center.api.json.error.Error;
import com.ndrlslz.configuration.center.api.json.error.ErrorBuilder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ConfigurationCenterControllerAdvice {
    @ResponseStatus(value = BAD_REQUEST)
    @ExceptionHandler(InvalidRequestBodyException.class)

    public Error invalidRequestBody(InvalidRequestBodyException exception) {
        return ErrorBuilder.badRequest(exception);
    }
}
