package com.ndrlslz.configuration.center.core.util;

import com.ndrlslz.configuration.center.core.model.AsyncResult;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class AsyncHelperTest {
    @Test
    public void shouldPerformAsyncCallable() {
        AsyncResult<String> result = AsyncHelper.async(() -> "result");

        assertThat(result.succeeded(), is(true));
        assertThat(result.getException(), nullValue());
        assertThat(result.getResult(), is("result"));
    }

    @Test
    public void shouldPerformAsyncCallableThatThrowException() {
        AsyncResult<Object> result = AsyncHelper.async(() -> {
            throw new RuntimeException();
        });

        assertThat(result.failed(), is(true));
        assertThat(result.getException(), notNullValue());
        assertThat(result.getResult(), nullValue());
    }

    @Test
    public void shouldPerformAsyncRunnable() {
        AsyncResult result = AsyncHelper.async(() -> {
        });

        assertThat(result.succeeded(), is(true));
        assertThat(result.getException(), nullValue());
        assertThat(result.getResult(), nullValue());
    }

    @Test
    public void shouldPerformAsyncRunnableThatThrowException() {
        AsyncResult result = AsyncHelper.async(() -> {
            throw new RuntimeException();
        });

        assertThat(result.failed(), is(true));
        assertThat(result.getException(), notNullValue());
        assertThat(result.getResult(), nullValue());
    }
}