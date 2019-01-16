package com.ndrlslz.configuration.center.sdk.client;

import com.ndrlslz.configuration.center.core.client.ConfigurationCenterClient;
import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import com.ndrlslz.configuration.center.core.exception.FirstConnectionTimeoutException;
import com.ndrlslz.configuration.center.sdk.listener.ConfigListener;

import static java.util.Objects.requireNonNull;

public class ConfigurationCenterTemplate implements ConfigurationCenterTemplateInterface {
    private ConfigurationCenterClient configurationCenterClient;
    private String application;
    private String environment;

    private ConfigurationCenterTemplate(Builder builder) {
        this.application = builder.application;
        this.environment = builder.environment;
        this.configurationCenterClient = builder.configurationCenterClient;
    }

    @Override
    public boolean isConnected() {
        return configurationCenterClient.isConnected();
    }

    @Override
    public String get(String name) {
        try {
            return configurationCenterClient.getProperty(application, environment, name).getValue();
        } catch (ConfigurationCenterException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void listen(String name, ConfigListener configListener) {
        try {
            configurationCenterClient.listenProperty(application, environment, name, node -> {
                String value = node.getValue();
                configListener.configChanged(value);
            });
        } catch (ConfigurationCenterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        configurationCenterClient.close();
    }

    public static class Builder {
        private String application;
        private String environment;
        private String connectionString;
        private Integer sessionTimeoutMs;
        private Integer connectionTimeoutMs;
        private ConfigurationCenterClient configurationCenterClient;

        public Builder application(String application) {
            this.application = application;
            return this;
        }

        public Builder environment(String environment) {
            this.environment = environment;
            return this;
        }

        public Builder connectionString(String connectionString) {
            this.connectionString = connectionString;
            return this;
        }

        public Builder sessionTimeoutMs(Integer sessionTimeoutMs) {
            this.sessionTimeoutMs = sessionTimeoutMs;
            return this;
        }

        public Builder connectionTimeoutMs(Integer connectionTimeoutMs) {
            this.connectionTimeoutMs = connectionTimeoutMs;
            return this;
        }

        public ConfigurationCenterTemplate build() {
            requireNonNull(connectionString, "connectionString cannot be null");
            requireNonNull(application, "application cannot be null");
            requireNonNull(environment, "environment cannot be null");

            try {
                configurationCenterClient = new ConfigurationCenterClient.Builder()
                        .connectionString(connectionString)
                        .connectionTimeoutMs(connectionTimeoutMs)
                        .sessionTimeoutMs(sessionTimeoutMs)
                        .fastFail(false)
                        .build();
            } catch (FirstConnectionTimeoutException ignored) {
            }

            return new ConfigurationCenterTemplate(this);
        }
    }
}
