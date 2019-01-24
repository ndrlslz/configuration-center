package com.ndrlslz.configuration.center.spring.model;

import com.ndrlslz.configuration.center.spring.annotation.Config;

abstract class Parent {
    @Config("age")
    private int age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
