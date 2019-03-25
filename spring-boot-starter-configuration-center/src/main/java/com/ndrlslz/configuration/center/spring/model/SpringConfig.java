package com.ndrlslz.configuration.center.spring.model;

import com.ndrlslz.configuration.center.spring.util.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import static java.util.Objects.nonNull;

public class SpringConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringConfig.class);
    private WeakReference<Object> beanRef;
    private Field field;
    private String propertyName;

    public SpringConfig(Object bean, Field field, String propertyName) {
        this.beanRef = new WeakReference<>(bean);
        this.field = field;
        this.propertyName = propertyName;
    }

    public WeakReference<Object> getBeanRef() {
        return beanRef;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void updateValue(String value) {
        Object bean = beanRef.get();

        if (nonNull(bean)) {
            try {
                ReflectionUtils.makeAccessible(field);
                field.set(bean, TypeConverter.convert(field, value));
            } catch (IllegalAccessException exception) {
                LOGGER.error(String.format("cannot set %s=%s for bean %s",
                        value, field.getName(), bean.getClass().getSimpleName()), exception);
            }
        }
    }
}
