package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.api.exception.ConfigurationCenterWrapperException;
import com.ndrlslz.configuration.center.api.exception.InvalidRequestBodyException;
import com.ndrlslz.configuration.center.api.json.error.Error;
import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.ndrlslz.configuration.center.api.json.error.ErrorBuilder.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ConfigurationCenterControllerAdvice {
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(InvalidRequestBodyException.class)
    public Error invalidRequestBody(InvalidRequestBodyException exception) {
        return badRequest(exception);
    }

    @ExceptionHandler(ConfigurationCenterWrapperException.class)
    public ResponseEntity<Error> configurationCenterWrapperException(ConfigurationCenterWrapperException exception) {
        ConfigurationCenterException configurationCenterException = exception.getConfigurationCenterException();

        Throwable cause = configurationCenterException.getCause();

        if (cause instanceof ConnectionLossException) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(internalServerError(cause));
        } else if (cause instanceof NodeExistsException) {
            return ResponseEntity.status(CONFLICT).body(conflict(cause));
        } else if (cause instanceof NoNodeException) {
            return ResponseEntity.status(NOT_FOUND).body(notFound(cause));
        } else if (cause instanceof KeeperException.BadVersionException) {
            return ResponseEntity.status(BAD_REQUEST).body(badRequest(cause));
        }

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(internalServerError(configurationCenterException));
    }
}
