package com.ndrlslz.configuration.center.core;

import com.ndrlslz.configuration.center.core.client.ConfigurationCenterClient;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;

public abstract class ConfigurationCenterBaseIntegrationTest {
    TestingServer testingServer;
    ConfigurationCenterClient configurationCenterClient;

    @Before
    public void setUp() throws Exception {
        testingServer = new TestingServer(8888);
        testingServer.start();

        configurationCenterClient = new ConfigurationCenterClient.Builder()
                .connectionString(testingServer.getConnectString())
                .sessionTimeoutMs(10000)
                .connectionTimeoutMs(10000)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        configurationCenterClient.close();
        testingServer.close();
    }
}
