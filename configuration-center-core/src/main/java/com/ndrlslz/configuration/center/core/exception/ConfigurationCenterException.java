package com.ndrlslz.configuration.center.core.exception;

public class ConfigurationCenterException extends Exception {
    ConfigurationCenterException(String message) {
        super(message);
    }

    public ConfigurationCenterException(String message, Throwable cause) {
        super(message, cause);
    }
}
