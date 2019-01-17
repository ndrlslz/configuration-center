package com.ndrlslz.configuration.center.sdk.listener;

@FunctionalInterface
public interface ConfigurationListener {
    void configChanged(String value);
}
