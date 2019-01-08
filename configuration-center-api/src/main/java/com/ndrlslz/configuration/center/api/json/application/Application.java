package com.ndrlslz.configuration.center.api.json.application;

public class Application {
    private String name;

    public Application() {

    }

    public Application(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Application{" +
                "name='" + name + '\'' +
                '}';
    }
}
