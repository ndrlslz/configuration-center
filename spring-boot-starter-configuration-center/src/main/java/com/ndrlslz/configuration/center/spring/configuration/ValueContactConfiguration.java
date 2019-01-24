package com.ndrlslz.configuration.center.spring.configuration;

import com.ndrlslz.configuration.center.spring.model.ValueContact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValueContactConfiguration {

    @Bean
    public ValueContact valueContact1() {
        ValueContact valueContact = new ValueContact();
        valueContact.setEmail("gmail@gmail.com");
        return valueContact;
    }
}
