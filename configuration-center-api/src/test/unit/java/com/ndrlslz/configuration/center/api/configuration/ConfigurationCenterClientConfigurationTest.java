package com.ndrlslz.configuration.center.api.configuration;

import com.ndrlslz.configuration.center.api.exception.ConfigurationCenterConnectionException;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

public class ConfigurationCenterClientConfigurationTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ConfigurationCenterClientProperty property;
    private ConfigurationCenterClientConfiguration configuration;
    private TestingServer testingServer;

    @Before
    public void setUp() throws Exception {
        testingServer = new TestingServer(6666);
        testingServer.start();

        property = new ConfigurationCenterClientProperty();
        property.setConnectionString(testingServer.getConnectString());
        configuration = new ConfigurationCenterClientConfiguration(property);
    }

    @After
    public void tearDown() throws Exception {
        testingServer.close();
    }

    @Test
    public void shouldCreateConfigurationCenterClient() {
        configuration.configurationCenterClient();
    }

    @Test
    public void shouldThrowExceptionGivenZookeeperIsDown() throws IOException {
        expectedException.expect(ConfigurationCenterConnectionException.class);
        expectedException.expectMessage("Connection timeout when first time connect to zookeeper");

        testingServer.close();
        configuration.configurationCenterClient();
    }
}