package com.ndrlslz.configuration.center.core;

import com.ndrlslz.configuration.center.core.client.ZookeeperClient;
import com.ndrlslz.configuration.center.core.exception.FirstConnectionTimeoutException;
import com.ndrlslz.configuration.center.core.model.Node;
import org.apache.zookeeper.KeeperException;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ZookeeperClientTest extends BaseIntegrationTest {
    private static final String PATH = "/test";

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

    @Test
    public void shouldCreateNodeWithEmptyValue() throws Exception {
        zookeeperClient.createNode(PATH);

        assertThat(zookeeperClient.getNode(PATH).getValue(), is(""));
    }

    @Test
    public void shouldCreateNodeWithValue() throws Exception {
        zookeeperClient.createNode(PATH, "value");

        assertThat(zookeeperClient.getNode(PATH).getValue(), is("value"));
    }

    @Test
    public void shouldGetNode() throws Exception {
        zookeeperClient.createNode(PATH);

        Node node = zookeeperClient.getNode(PATH);

        assertThat(node.getValue(), is(not(nullValue())));
        assertThat(node.getAversion(), is(not(nullValue())));
        assertThat(node.getCtime(), is(not(nullValue())));
        assertThat(node.getCversion(), is(not(nullValue())));
        assertThat(node.getCzxid(), is(not(nullValue())));
        assertThat(node.getDataLength(), is(not(nullValue())));
        assertThat(node.getEphemeralOwner(), is(not(nullValue())));
        assertThat(node.getMtime(), is(not(nullValue())));
        assertThat(node.getMzxid(), is(not(nullValue())));
        assertThat(node.getNumChildren(), is(not(nullValue())));
        assertThat(node.getPzxid(), is(not(nullValue())));
        assertThat(node.getVersion(), is(not(nullValue())));
    }

    @Test
    public void shouldUpdateNode() throws Exception {
        zookeeperClient.createNode(PATH, "value");
        Node node = zookeeperClient.getNode(PATH);
        assertThat(node.getValue(), is("value"));

        zookeeperClient.updateNode(PATH, "new_value", node.getVersion());

        assertThat(zookeeperClient.getNode(PATH).getValue(), is("new_value"));
    }

    @Test
    public void shouldDeleteNodeWithVersion() throws Exception {
        zookeeperClient.createNode(PATH);
        Node node = zookeeperClient.getNode(PATH);

        zookeeperClient.deleteNode(PATH, node.getVersion());

        try {
            zookeeperClient.getNode(PATH);
        } catch (KeeperException.NoNodeException ignored) {
        }
    }

    @Test(expected = KeeperException.NoNodeException.class)
    public void shouldDeleteNode() throws Exception {
        zookeeperClient.createNode(PATH);

        zookeeperClient.deleteNode(PATH);

        zookeeperClient.getNode(PATH);
    }
}