package com.ndrlslz.configuration.center.core;

import com.ndrlslz.configuration.center.core.client.ZookeeperClient;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;

public abstract class ZookeeperClientBaseIntegrationTest {
    TestingServer testingServer;
    ZookeeperClient zookeeperClient;

    @Before
    public void setUp() throws Exception {
        testingServer = new TestingServer(8888);
        testingServer.start();

        zookeeperClient = new ZookeeperClient.Builder()
                .connectionString(testingServer.getConnectString())
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(5000)
                .fastFail(true)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        zookeeperClient.close();
        testingServer.close();
    }
}
