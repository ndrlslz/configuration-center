package com.ndrlslz.configuration.center.spring.configuration;

import com.ndrlslz.configuration.center.spring.model.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfiguration {
    @Bean
    public Person person1() {
        Person person = new Person();
        person.setAddress("yo");
        person.setName("Nick");
        return person;
    }
}
