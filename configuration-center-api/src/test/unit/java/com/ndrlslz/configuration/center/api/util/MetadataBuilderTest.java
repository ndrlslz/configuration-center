package com.ndrlslz.configuration.center.api.util;

import com.ndrlslz.configuration.center.api.json.common.Metadata;
import com.ndrlslz.configuration.center.core.model.Page;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MetadataBuilderTest {
    @Test
    public void shouldGenerateMetadata() {
        Page<String> page = new Page.Builder<String>()
                .withContent(Arrays.asList("1", "2", "3", "4", "5"))
                .withSize(10)
                .withNumber(0)
                .withTotalElements(5)
                .withTotalPages(1)
                .build();

        Metadata metadata = new MetadataBuilder().withPage(page).build();

        assertThat(metadata.getSize(), is(10));
        assertThat(metadata.getNumber(), is(0));
        assertThat(metadata.getTotalElements(), is(5));
        assertThat(metadata.getTotalPages(), is(1));
    }
}