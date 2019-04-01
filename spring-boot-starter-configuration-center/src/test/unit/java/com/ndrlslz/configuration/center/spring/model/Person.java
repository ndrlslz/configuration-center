package com.ndrlslz.configuration.center.spring.model;

public class Person {
    private String name;
    private int age;
    private boolean isMan;

    public Person(String name, int age, boolean man) {
        this.name = name;
        this.age = age;
        this.isMan = man;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isMan() {
        return isMan;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setIsMan(boolean isMan) {
        this.isMan = isMan;
    }
}
