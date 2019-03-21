package com.ndrlslz.configuration.center.sdk;

import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import com.ndrlslz.configuration.center.sdk.exception.ZookeeperNodeNotExistsException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationTemplateTest extends IntegrationTestBase {

    @Test
    public void shouldGetProperty() throws ConfigurationCenterException {
        configurationCenterClient.createProperty(APPLICATION, ENVIRONMENT, "key", "value");

        String value = configurationTemplate.get("key");

        assertThat(value, is("value"));
    }

    @Test(expected = ZookeeperNodeNotExistsException.class)
    public void shouldThrowExceptionWhenGetPropertyGivenNodeNotExists() {
        configurationTemplate.get("key.not.exists");
    }

    @Test
    public void shouldIgnoreDefaultValueWhenGetPropertyGivenPropertyExists() throws ConfigurationCenterException {
        configurationCenterClient.createProperty(APPLICATION, ENVIRONMENT, "key", "value");

        String value = configurationTemplate.get("key", "default_value");

        assertThat(value, is("value"));
    }

    @Test
    public void shouldGetPropertyDefaultValueGivenNodeNotExists() {
        String value = configurationTemplate.get("key", "default_value");

        assertThat(value, is("default_value"));
    }

    @Test
    public void shouldListenProperty() throws ConfigurationCenterException, InterruptedException {
        configurationCenterClient.createProperty(APPLICATION, ENVIRONMENT, "version", "1");
        ArrayList<String> result = new ArrayList<>();

        CountDownLatch latch = new CountDownLatch(2);
        configurationTemplate.listen(this, "version", value -> {
            result.add(value);
            latch.countDown();
        });
        configurationCenterClient.updateProperty(APPLICATION, ENVIRONMENT, "version", "2"
                , configurationCenterClient.getProperty(APPLICATION, ENVIRONMENT, "version").getVersion());
        latch.await();

        assertThat(result, hasItems("1", "2"));
    }
}
