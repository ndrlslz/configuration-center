package com.ndrlslz.configuration.center.spring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "configuration-center")
public class ConfigTemplateProperties {
    private String application;
    private String environment;
    private String connectionString;
    private Integer sessionTimeoutMs;
    private Integer connectionTimeoutMs;

    public String getConnectionString() {
        return connectionString;
    }

    public Integer getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public Integer getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public void setSessionTimeoutMs(Integer sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public void setConnectionTimeoutMs(Integer connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }
}
