package com.ndrlslz.configuration.center.spring.model;

import com.ndrlslz.configuration.center.spring.util.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.util.Objects.nonNull;

public class SpringConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringConfig.class);
    private WeakReference<Object> beanRef;
    private Field field;
    private Method method;
    private Class<?> methodParameterType;
    private String propertyName;

    public SpringConfig(Object bean, Field field, String propertyName) {
        this.beanRef = new WeakReference<>(bean);
        this.field = field;
        this.propertyName = propertyName;
    }

    public SpringConfig(Object bean, Method method, Class<?> methodParameterType, String propertyName) {
        this.beanRef = new WeakReference<>(bean);
        this.method = method;
        this.methodParameterType = methodParameterType;
        this.propertyName = propertyName;

    }

    public WeakReference<Object> getBeanRef() {
        return beanRef;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void updateValue(String value) {
        if (isField()) {
            updateFieldValue(value);
        } else {
            updateMethodValue(value);
        }
    }

    private boolean isField() {
        return field != null;
    }

    private void updateFieldValue(String value) {
        Object bean = beanRef.get();

        if (nonNull(bean)) {
            try {
                ReflectionUtils.makeAccessible(field);
                field.set(bean, TypeConverter.convert(value, field.getType()));
            } catch (IllegalAccessException exception) {
                LOGGER.error(String.format("fail to set %s=%s for bean %s",
                        field.getName(), value, bean.getClass().getSimpleName()), exception);
            }
        }
    }

    private void updateMethodValue(String value) {
        Object bean = beanRef.get();

        if (nonNull(bean)) {
            try {
                Object[] params = {TypeConverter.convert(value, methodParameterType)};
                ReflectionUtils.makeAccessible(method);
                method.invoke(bean, params);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                LOGGER.error(String.format("fail to invoke method %s.%s to set config value %s",
                        bean.getClass().getName(), method.getName(), value), exception);

            }
        }
    }
}
