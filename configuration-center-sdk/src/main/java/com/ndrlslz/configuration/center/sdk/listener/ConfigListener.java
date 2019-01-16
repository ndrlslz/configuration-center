package com.ndrlslz.configuration.center.sdk.listener;

@FunctionalInterface
public interface ConfigListener {
    void configChanged(String value);
}
