package com.ndrlslz.configuration.center.spring.configuration;

import com.ndrlslz.configuration.center.sdk.client.ConfigurationTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigTemplateConfiguration {
    @Bean
    public ConfigurationTemplate configurationTemplate() {
        return new ConfigurationTemplate.Builder()
                .environment("dev")
                .application("customer-api")
                .connectionString("localhost:2181")
                .build();
    }
}
