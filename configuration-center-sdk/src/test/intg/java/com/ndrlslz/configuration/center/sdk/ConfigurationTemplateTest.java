package com.ndrlslz.configuration.center.sdk;

import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import com.ndrlslz.configuration.center.sdk.exception.ZookeeperNodeNotExistsException;
import com.ndrlslz.configuration.center.sdk.storage.ZookeeperStorage;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
    public void shouldGetPropertyDefaultValueGivenNodeNotExists() {
        String value = configurationTemplate.get("key", "default_value");

        assertThat(value, is("default_value"));
    }

    @Test
    public void shouldListenProperty() throws ConfigurationCenterException, InterruptedException {
        configurationCenterClient.createProperty(APPLICATION, ENVIRONMENT, "version", "1");
        ArrayList<String> result = new ArrayList<>();

        configurationTemplate.listen("version", result::add);
        configurationCenterClient.updateProperty(APPLICATION, ENVIRONMENT, "version", "2"
                , configurationCenterClient.getProperty(APPLICATION, ENVIRONMENT, "version").getVersion());
        TimeUnit.MILLISECONDS.sleep(100);

        assertThat(result, hasItems("1", "2"));
    }
}
