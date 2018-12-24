package com.ndrlslz.configuration.center.core.client;

import com.ndrlslz.configuration.center.core.exception.FirstConnectionTimeoutException;
import com.ndrlslz.configuration.center.core.model.Node;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ZookeeperClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperClient.class);
    private static final int FIRST_CONNECTION_TIMEOUT_S = 10;
    private static final int DEFAULT_SESSION_TIMEOUT_MS = 60 * 1000;
    private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 15 * 1000;
    private static final int BASE_SLEEP_TIME_MS = 5000;
    private static final int MAX_RETRIES = 29;
    private static final String NAMESPACE = "configuration-center";
    private CuratorFramework curatorFramework;

    private ZookeeperClient(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    public boolean isConnected() {
        return curatorFramework.getZookeeperClient().isConnected();
    }

    public void close() {
        curatorFramework.close();
    }

    public String createNode(String path, String value) throws Exception {
        return createNode(path, value.getBytes());
    }

    public String createNode(String path) throws Exception {
        return createNode(path, new byte[0]);
    }

    private String createNode(String path, byte[] value) throws Exception {
        return curatorFramework.create()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(path, value);
    }

    public Node getNode(String path) throws Exception {
        Stat stat = new Stat();
        byte[] value = curatorFramework.getData()
                .storingStatIn(stat)
                .forPath(path);

        return new Node.Builder()
                .withStat(stat)
                .withValue(new String(value))
                .build();
    }

    public Node updateNode(String path, String value, int version) throws Exception {
        Stat stat = curatorFramework.setData()
                .withVersion(version)
                .forPath(path, value.getBytes());

        return new Node.Builder()
                .withValue(value)
                .withStat(stat)
                .build();
    }

    public void deleteNode(String path, int version) throws Exception {
        curatorFramework.delete()
                .deletingChildrenIfNeeded()
                .withVersion(version)
                .forPath(path);
    }

    public void deleteNode(String path) throws Exception {
        curatorFramework.delete()
                .deletingChildrenIfNeeded()
                .forPath(path);
    }

    public static class Builder {
        private int sessionTimeoutMs = DEFAULT_SESSION_TIMEOUT_MS;
        private int connectionTimeoutMs = DEFAULT_CONNECTION_TIMEOUT_MS;
        private String connectionString;

        public Builder connectionString(String connectionString) {
            this.connectionString = connectionString;
            return this;
        }

        public Builder sessionTimeoutMs(int sessionTimeoutMs) {
            this.sessionTimeoutMs = sessionTimeoutMs;
            return this;
        }

        public Builder connectionTimeoutMs(int connectionTimeoutMs) {
            this.connectionTimeoutMs = connectionTimeoutMs;
            return this;
        }

        public ZookeeperClient build() throws FirstConnectionTimeoutException {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES);

            CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                    .connectString(connectionString)
                    .sessionTimeoutMs(sessionTimeoutMs)
                    .connectionTimeoutMs(connectionTimeoutMs)
                    .retryPolicy(retryPolicy)
                    .namespace(NAMESPACE)
                    .build();

            curatorFramework.start();

            try {
                curatorFramework.blockUntilConnected(FIRST_CONNECTION_TIMEOUT_S, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                LOGGER.error("Interrupted Exception when connect to zookeeper.", e);
                curatorFramework.close();
            }

            if (!curatorFramework.getZookeeperClient().isConnected()) {
                curatorFramework.close();
                throw new FirstConnectionTimeoutException("Connection timeout when first time connect to zookeeper");
            }

            return new ZookeeperClient(curatorFramework);
        }
    }
}