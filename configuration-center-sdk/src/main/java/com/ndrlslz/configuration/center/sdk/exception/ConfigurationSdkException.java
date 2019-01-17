package com.ndrlslz.configuration.center.sdk.exception;

public abstract class ConfigurationSdkException extends RuntimeException {
    ConfigurationSdkException(String message) {
        super(message);
    }

    ConfigurationSdkException(String message, Throwable cause) {
        super(message, cause);
    }
}
