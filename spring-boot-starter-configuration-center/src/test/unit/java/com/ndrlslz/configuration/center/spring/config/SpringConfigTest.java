package com.ndrlslz.configuration.center.spring.config;

import com.ndrlslz.configuration.center.spring.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SpringConfigTest {
    private Person person;

    @Before
    public void setUp() {
        person = new Person("Tom", 23, true);
    }

    @Test
    public void shouldUpdateFieldValue() {
        SpringConfig nameConfig = fieldConfigOf("name");
        SpringConfig ageConfig = fieldConfigOf("age");
        SpringConfig isManConfig = fieldConfigOf("isMan");

        nameConfig.updateValue("Nick");
        ageConfig.updateValue("36");
        isManConfig.updateValue("false");

        assertThat(person.getName(), is("Nick"));
        assertThat(person.getAge(), is(36));
        assertThat(person.isMan(), is(false));
    }

    @Test
    public void shouldUpdateMethodValue() {
        SpringConfig nameConfig = methodConfigOf("setName", String.class);
        SpringConfig ageConfig = methodConfigOf("setAge", int.class);
        SpringConfig isManConfig = methodConfigOf("setIsMan", boolean.class);

        nameConfig.updateValue("Nick");
        ageConfig.updateValue("36");
        isManConfig.updateValue("false");

        assertThat(person.getName(), is("Nick"));
        assertThat(person.getAge(), is(36));
        assertThat(person.isMan(), is(false));
    }

    private SpringConfig fieldConfigOf(String fieldName) {
        try {
            return new SpringConfig(person, person.getClass().getDeclaredField(fieldName), null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("");
        }
    }

    private SpringConfig methodConfigOf(String methodName, Class<?> type) {
        Method nameMethod;
        try {
            nameMethod = person.getClass().getDeclaredMethod(methodName, type);
            return new SpringConfig(person, nameMethod, type, null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("");
        }
    }
}