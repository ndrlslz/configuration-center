package com.ndrlslz.configuration.center.spring.app.model;

import com.ndrlslz.configuration.center.spring.annotation.Config;

abstract class Person {
    @Config("name")
    private String name;

}
