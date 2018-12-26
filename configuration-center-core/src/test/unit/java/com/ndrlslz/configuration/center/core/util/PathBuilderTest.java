package com.ndrlslz.configuration.center.core.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PathBuilderTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldGeneratePath() {
        String path = PathBuilder.pathOf("customer-api", "dev", "property");

        assertThat(path, is("/customer-api/dev/property"));
    }

    @Test
    public void shouldThrowExceptionGivenEmptyArgument() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("path cannot be empty");

        PathBuilder.pathOf();
    }

    @Test
    public void shouldThrowNullPointExceptionGivenNullArgument() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("path cannot be null");

        PathBuilder.pathOf("123", null);
    }

    @Test
    public void shouldThrowExceptionGivenPathContainsSlash() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("/test cannot contains slash special char");

        PathBuilder.pathOf("/test");
    }
}