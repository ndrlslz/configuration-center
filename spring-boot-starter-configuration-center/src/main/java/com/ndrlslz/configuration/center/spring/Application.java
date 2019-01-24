package com.ndrlslz.configuration.center.spring;

import com.ndrlslz.configuration.center.spring.model.Person;
import com.ndrlslz.configuration.center.spring.model.ValueContact;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class);
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);

        Person person1 = (Person) applicationContext.getBean("person1");
        System.out.println(person1);

        ValueContact valueContact = (ValueContact) applicationContext.getBean("valueContact");
        System.out.println(valueContact);

        ValueContact valueContact1 = (ValueContact) applicationContext.getBean("valueContact1");
        System.out.println(valueContact1);

    }
}
