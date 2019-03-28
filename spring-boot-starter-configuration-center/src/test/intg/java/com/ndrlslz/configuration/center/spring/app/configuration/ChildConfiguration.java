package com.ndrlslz.configuration.center.spring.app.configuration;

import com.ndrlslz.configuration.center.spring.annotation.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChildConfiguration extends ParentConfiguration {
    private String address;

    @Config("javaer")
    private boolean javaer;

    @Config("email")
    private String emailAddress;

    public String getAddress() {
        return address;
    }

    public boolean isJavaer() {
        return javaer;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Config(value = "address", refresh = true)
    public void setAddress(String address) {
        this.address = address;
    }
}
