package com.ndrlslz.configuration.center.spring.util;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TypeConverterTest {
    @Test
    public void shouldConvertStringType() {
        Object result = TypeConverter.convert("123", String.class);

        assertThat(result, is("123"));
    }

    @Test
    public void shouldConvertIntegerType() {
        Object result = TypeConverter.convert("123", Integer.class);

        assertThat(result, is(123));
    }

    @Test
    public void shouldConvertBooleanType() {
        Object result = TypeConverter.convert("true", Boolean.class);

        assertThat(result, is(true));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionSinceTypeNotSupported() {
        TypeConverter.convert("true", Map.class);
    }
}