package com.ndrlslz.configuration.center.api.exception;

abstract class ConfigurationCenterApiException extends RuntimeException {
    ConfigurationCenterApiException(String message) {
        super(message);
    }
}
