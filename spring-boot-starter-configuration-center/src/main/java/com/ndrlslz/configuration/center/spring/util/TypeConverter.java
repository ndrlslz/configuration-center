package com.ndrlslz.configuration.center.spring.util;

import java.lang.reflect.Field;

public class TypeConverter {
    public static Object convert(Field field, String value) {
        Object result;
        Class<?> type = field.getType();
        if (type == String.class) {
            result = value;
        } else if (type == Integer.class || type == int.class) {
            result = Integer.valueOf(value);
        } else if (type == Boolean.class || type == boolean.class) {
            result = Boolean.valueOf(value);
        } else {
            throw new RuntimeException("type not supported");
        }
        return result;
    }

}
