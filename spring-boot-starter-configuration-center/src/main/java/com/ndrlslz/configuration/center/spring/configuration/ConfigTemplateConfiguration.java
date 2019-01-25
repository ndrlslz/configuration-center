package com.ndrlslz.configuration.center.spring.configuration;

import com.ndrlslz.configuration.center.sdk.client.ConfigurationTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;

@Configuration
public class ConfigTemplateConfiguration {
    private final ConfigTemplateProperties properties;

    @Autowired
    public ConfigTemplateConfiguration(ConfigTemplateProperties properties) {
        this.properties = properties;
    }

    @Bean(destroyMethod = "close")
    public ConfigurationTemplate configurationTemplate() {
        return new ConfigurationTemplate.Builder()
                .application(properties.getApplication())
                .environment(properties.getEnvironment())
                .connectionString(properties.getConnectionString())
                .sessionTimeoutMs(properties.getSessionTimeoutMs())
                .connectionTimeoutMs(properties.getConnectionTimeoutMs())
                .build();
    }
}
