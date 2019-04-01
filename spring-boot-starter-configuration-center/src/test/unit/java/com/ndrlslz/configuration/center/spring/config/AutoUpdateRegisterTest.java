package com.ndrlslz.configuration.center.spring.config;

import com.ndrlslz.configuration.center.sdk.client.ConfigurationTemplate;
import com.ndrlslz.configuration.center.sdk.listener.ConfigurationListener;
import com.ndrlslz.configuration.center.spring.model.Person;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


public class AutoUpdateRegisterTest {
    private Person person;
    private AutoUpdateRegister autoUpdateRegister;
    private ConfigurationTemplate configurationTemplate;

    @Before
    public void setUp() {
        person = new Person("Tom", 23, true);

        configurationTemplate = Mockito.mock(ConfigurationTemplate.class);
        autoUpdateRegister = new AutoUpdateRegister(configurationTemplate);
    }

    @Test
    public void shouldRegisterSpringConfig() {
        SpringConfig nameConfig = fieldConfigOf("name");

        autoUpdateRegister.register(nameConfig);

        verify(configurationTemplate).listen(any(), any(), any(ConfigurationListener.class));
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