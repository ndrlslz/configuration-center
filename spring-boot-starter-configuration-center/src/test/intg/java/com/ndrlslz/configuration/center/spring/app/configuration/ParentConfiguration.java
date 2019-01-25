package com.ndrlslz.configuration.center.spring.app.configuration;

import com.ndrlslz.configuration.center.spring.annotation.Config;

public class ParentConfiguration {
    @Config("name")
    private String name;

    @Config("age")
    private int age;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
