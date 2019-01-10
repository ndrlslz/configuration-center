package com.ndrlslz.configuration.center.api.exception;

public class ConfigurationCenterConnectionException extends ConfigurationCenterApiException {
    public ConfigurationCenterConnectionException(String message) {
        super(message);
    }

    public ConfigurationCenterConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
