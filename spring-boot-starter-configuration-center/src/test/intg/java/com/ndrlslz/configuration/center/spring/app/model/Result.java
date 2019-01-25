package com.ndrlslz.configuration.center.spring.app.model;

public class Result {
    private String address;

    private boolean javaer;

    private String emailAddress;

    private String name;

    private int age;

    private String app;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int isAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isJavaer() {
        return javaer;
    }

    public void setJavaer(boolean javaer) {
        this.javaer = javaer;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
