package com.ndrlslz.configuration.center.api.util;

import com.ndrlslz.configuration.center.api.json.application.Application;
import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Type;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DataBuilderTest {
    @Test
    public void shouldGenerateData() {
        Data<Application> data = new DataBuilder<Application>()
                .withAttributes(new Application("test"))
                .withType(Type.APPLICATION)
                .withRelationship("key1", "value1")
                .withRelationship("key2", "value2")
                .build();

        assertThat(data.getType(), is(Type.APPLICATION));
        assertThat(data.getAttributes().getName(), is("test"));
        assertThat(data.getRelationships().size(), is(2));
        assertThat(data.getRelationships().get("key1"), is("value1"));
        assertThat(data.getRelationships().get("key2"), is("value2"));
    }
}