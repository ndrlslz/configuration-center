package com.ndrlslz.configuration.center.api.configuration;

import com.ndrlslz.configuration.center.api.exception.ConfigurationCenterConnectionException;
import com.ndrlslz.configuration.center.core.client.ConfigurationCenterClient;
import com.ndrlslz.configuration.center.core.exception.FirstConnectionTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationCenterClientConfiguration {
    private final ConfigurationCenterClientProperty configurationCenterClientProperty;

    @Autowired
    public ConfigurationCenterClientConfiguration(ConfigurationCenterClientProperty configurationCenterClientProperty) {
        this.configurationCenterClientProperty = configurationCenterClientProperty;
    }

    @Bean(destroyMethod = "close")
    public ConfigurationCenterClient configurationCenterClient() {
        try {
            return new ConfigurationCenterClient.Builder()
                    .connectionString(configurationCenterClientProperty.getConnectionString())
                    .sessionTimeoutMs(configurationCenterClientProperty.getSessionTimeoutMs())
                    .connectionTimeoutMs(configurationCenterClientProperty.getConnectionTimeoutMs())
                    .build();
        } catch (FirstConnectionTimeoutException e) {
            throw new ConfigurationCenterConnectionException(e.getMessage(), e);
        }
    }
}
