package com.ndrlslz.configuration.center.api.exception;

abstract class ConfigurationCenterApiException extends RuntimeException {
    ConfigurationCenterApiException(String message) {
        super(message);
    }

    ConfigurationCenterApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
