package com.ndrlslz.configuration.center.spring.model;

import com.ndrlslz.configuration.center.spring.annotation.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Person extends Parent {
    @Config(value = "name")
    private String name;
    @Config(value = "address_two")
    private String address;

    @Value("${email}")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", age='" + getAge() + '\'' +
                '}';
    }
}
