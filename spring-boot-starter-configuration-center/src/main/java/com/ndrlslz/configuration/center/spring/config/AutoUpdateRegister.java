package com.ndrlslz.configuration.center.spring.config;

import com.ndrlslz.configuration.center.sdk.client.ConfigurationTemplate;
import com.ndrlslz.configuration.center.spring.util.TypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

@Configuration
public class AutoUpdateRegister {
    private final ConfigurationTemplate configurationTemplate;

    @Autowired
    public AutoUpdateRegister(ConfigurationTemplate configurationTemplate) {
        this.configurationTemplate = configurationTemplate;
    }

    public void register(Object bean, Field field, String propertyName) {
        WeakReference<Object> beanRef = new WeakReference<>(bean);

        //TODO there is a issue if two bean use same property
        configurationTemplate.listen(beanRef, propertyName, updatedValue -> {
            Object originalBean = beanRef.get();
            if (originalBean != null) {
                try {
                    field.set(originalBean, TypeConverter.convert(field, updatedValue));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                configurationTemplate.unListen(this, propertyName);
            }
        });
    }

}