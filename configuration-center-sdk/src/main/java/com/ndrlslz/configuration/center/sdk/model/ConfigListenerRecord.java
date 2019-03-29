package com.ndrlslz.configuration.center.sdk.model;

import com.ndrlslz.configuration.center.sdk.listener.ConfigurationListener;

public class ConfigListenerRecord {
    private Object object;
    private String property;
    private ConfigurationListener configurationListener;

    public ConfigListenerRecord(Object object, String property, ConfigurationListener configurationListener) {
        this.object = object;
        this.property = property;
        this.configurationListener = configurationListener;
    }

    public Object getObject() {
        return object;
    }

    public String getProperty() {
        return property;
    }

    public ConfigurationListener getConfigurationListener() {
        return configurationListener;
    }
}
