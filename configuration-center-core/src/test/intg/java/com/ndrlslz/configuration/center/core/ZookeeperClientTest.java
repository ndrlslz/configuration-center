package com.ndrlslz.configuration.center.core;

import com.ndrlslz.configuration.center.core.client.ZookeeperClient;
import com.ndrlslz.configuration.center.core.exception.FirstConnectionTimeoutException;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ZookeeperClientTest extends BaseIntegrationTest {
    @Test
    public void shouldConnectZookeeperGivenZookeeperStarted() {
        assertThat(zookeeperClient.isConnected(), is(true));
    }

    @Test
    public void shouldNotConnectZookeeperGivenZookeeperStopped() throws IOException, InterruptedException {
        testingServer.close();
        TimeUnit.SECONDS.sleep(1);

        assertThat(zookeeperClient.isConnected(), is(false));
    }

    @Test(expected = FirstConnectionTimeoutException.class)
    public void shouldThrowExceptionWhenFirstTimeConnectZookeeperGivenZookeeperStopped() throws IOException, InterruptedException, FirstConnectionTimeoutException {
        testingServer.close();
        TimeUnit.SECONDS.sleep(1);

        zookeeperClient = new ZookeeperClient.Builder()
                .connectionString(testingServer.getConnectString())
                .build();
    }

    @Test(expected = FirstConnectionTimeoutException.class)
    public void shouldThrowExceptionWhenInterruptThread() throws FirstConnectionTimeoutException {
        Thread.currentThread().interrupt();

        zookeeperClient = new ZookeeperClient.Builder()
                .connectionString(testingServer.getConnectString())
                .build();
    }
}