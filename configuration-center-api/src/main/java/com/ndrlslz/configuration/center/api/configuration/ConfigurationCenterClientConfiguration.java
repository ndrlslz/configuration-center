package com.ndrlslz.configuration.center.api.configuration;

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

    //TODO should handle first connection timeout exception
    @Bean
    public ConfigurationCenterClient configurationCenterClient() {
        try {
            return new ConfigurationCenterClient.Builder()
                    .connectionString(configurationCenterClientProperty.getConnectionString())
                    .sessionTimeoutMs(configurationCenterClientProperty.getSessionTimeoutMs())
                    .connectionTimeoutMs(configurationCenterClientProperty.getConnectionTimeoutMs())
                    .build();
        } catch (FirstConnectionTimeoutException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
