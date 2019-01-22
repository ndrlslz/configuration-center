package com.ndrlslz.configuration.center.sdk;

import com.ndrlslz.configuration.center.core.client.ConfigurationCenterClient;
import com.ndrlslz.configuration.center.sdk.client.ConfigurationTemplate;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;

abstract public class IntegrationTestBase {
    static final String APPLICATION = "customer-api";
    static final String ENVIRONMENT = "dev";
    private TestingServer testingServer;
    ConfigurationTemplate configurationTemplate;
    ConfigurationCenterClient configurationCenterClient;

    @Before
    public void setUp() throws Exception {
        testingServer = new TestingServer(7777);
        testingServer.start();

        configurationCenterClient = new ConfigurationCenterClient.Builder()
                .connectionString(testingServer.getConnectString())
                .sessionTimeoutMs(10000)
                .connectionTimeoutMs(10000)
                .build();

        configurationTemplate = new ConfigurationTemplate.Builder()
                .connectionString(testingServer.getConnectString())
                .application(APPLICATION)
                .environment(ENVIRONMENT)
                .sessionTimeoutMs(10000)
                .connectionTimeoutMs(10000)
                .build();

        configurationCenterClient.createApplication(APPLICATION);
        configurationCenterClient.createEnvironment(APPLICATION, ENVIRONMENT);
    }

    @After
    public void tearDown() throws Exception {
        configurationCenterClient.close();
        configurationTemplate.close();
        testingServer.close();
    }
}