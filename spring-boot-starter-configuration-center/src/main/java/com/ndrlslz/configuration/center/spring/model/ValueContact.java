package com.ndrlslz.configuration.center.spring.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValueContact {
    @Value("${email}")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ValueContact{" +
                "email='" + email + '\'' +
                '}';
    }
}
