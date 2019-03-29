package com.ndrlslz.configuration.center.sdk;

import com.ndrlslz.configuration.center.sdk.exception.ConfigurationNotFoundException;
import com.ndrlslz.configuration.center.sdk.utils.DisasterRecoveryFileBuilder;
import com.ndrlslz.configuration.center.sdk.utils.MemoryCacheFileBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationTemplateFailoverTest extends IntegrationFailoverTestBase {
    @Test
    public void shouldNotConnectZookeeper() {
        createConfigurationTemplate();
        boolean connected = configurationTemplate.isConnected();

        assertThat(connected, is(false));
    }

    @Test
    public void shouldGetPropertyFromMemoryCacheFile() throws IOException {
        new MemoryCacheFileBuilder()
                .property("key", "value1")
                .create();
        createConfigurationTemplate();

        String value = configurationTemplate.get("key");
        assertThat(value, is("value1"));
    }

    @Test(expected = ConfigurationNotFoundException.class)
    public void shouldThrowExceptionGivenPropertyNotExistsInMemoryCacheFile() throws IOException {
        new MemoryCacheFileBuilder()
                .property("key", "value3")
                .create();
        createConfigurationTemplate();

        configurationTemplate.get("key_not_exists");
    }

    @Test
    public void shouldGetPropertyFromDisasterRecoveryFile() throws IOException {
        new DisasterRecoveryFileBuilder()
                .property("key", "value2")
                .create();
        createConfigurationTemplate();

        String value = configurationTemplate.get("key");
        assertThat(value, is("value2"));
    }

    @Test(expected = ConfigurationNotFoundException.class)
    public void shouldThrowExceptionGivenPropertyNotExistsInDisasterRecoveryFile() throws IOException {
        new DisasterRecoveryFileBuilder()
                .property("key", "value4")
                .create();
        createConfigurationTemplate();

        configurationTemplate.get("key_not_exists");
    }

    @Test
    public void shouldGetPropertyFromDisasterRecoveryFileGivenBothExists() throws IOException {
        new MemoryCacheFileBuilder()
                .property("key", "memory-cache")
                .create();
        new DisasterRecoveryFileBuilder()
                .property("key", "disaster-recovery")
                .create();
        createConfigurationTemplate();

        String value = configurationTemplate.get("key");
        assertThat(value, is("disaster-recovery"));
    }

    @Test
    public void shouldGetDefaultPropertyGivenBothFileNotExists() {
        createConfigurationTemplate();

        String value = configurationTemplate.get("key", "default_value");
        assertThat(value, is("default_value"));
    }

    @Test
    public void shouldListenPropertyFromMemoryCacheFile() throws IOException, InterruptedException {
        new MemoryCacheFileBuilder()
                .property("key", "value")
                .create();
        createConfigurationTemplate();

        ArrayList<String> result = new ArrayList<>();

        CountDownLatch latch = new CountDownLatch(1);
        configurationTemplate.listen(this, "key", value -> {
            result.add(value);
            latch.countDown();
        });

        latch.await();
        assertThat(result, hasItem("value"));
    }

    @Test(expected = ConfigurationNotFoundException.class)
    public void shouldThrowExceptionWhenListenPropertyGivenBothFileNotExists() {
        createConfigurationTemplate();

        configurationTemplate.listen(this, "key", System.out::println);
    }

    @Test
    public void shouldListenPropertyOnceReconnectZookeeper() throws Exception {
        new DisasterRecoveryFileBuilder()
                .property("key", "value")
                .create();
        createConfigurationTemplate();

        ArrayList<String> result = new ArrayList<>();

        CountDownLatch latch = new CountDownLatch(1);

        configurationTemplate.listen(this, "key", value -> {
            result.add(value);
            latch.countDown();
        });
        latch.await();
        assertThat(result, hasItem("value"));

        testingServer.restart();
        createConfigurationCenterClient();

        configurationCenterClient.createApplication(APPLICATION);
        configurationCenterClient.createEnvironment(APPLICATION, ENVIRONMENT);
        configurationCenterClient.createProperty(APPLICATION, ENVIRONMENT, "key", "new_value");

        TimeUnit.MILLISECONDS.sleep(500);
        assertThat(result, hasItems("value", "new_value"));

    }
}