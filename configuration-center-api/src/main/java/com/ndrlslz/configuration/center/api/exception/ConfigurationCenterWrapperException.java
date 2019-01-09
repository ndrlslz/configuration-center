package com.ndrlslz.configuration.center.api.exception;

import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;

public class ConfigurationCenterWrapperException extends ConfigurationCenterApiException {
    private ConfigurationCenterException configurationCenterException;

    public ConfigurationCenterWrapperException(String message, ConfigurationCenterException configurationCenterException) {
        super(message);
        this.configurationCenterException = configurationCenterException;
    }

    public ConfigurationCenterException getConfigurationCenterException() {
        return configurationCenterException;
    }
}
