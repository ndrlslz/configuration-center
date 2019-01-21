package com.ndrlslz.configuration.center.core.client;

import com.ndrlslz.configuration.center.core.exception.FirstConnectionTimeoutException;
import com.ndrlslz.configuration.center.core.listener.NodeListener;
import com.ndrlslz.configuration.center.core.model.Node;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

public class ZookeeperClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperClient.class);
    private static final Integer FIRST_CONNECTION_TIMEOUT_S = 10;
    private static final int DEFAULT_SESSION_TIMEOUT_MS = 60 * 1000;
    private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 5 * 1000;
    private static final int SLEEP_MS_BETWEEN_RETRY = 1000;
    public static final String NAMESPACE = "configuration-center";
    private static final boolean DEFAULT_FAST_FAIL = true;
    private static final ConcurrentHashMap<String, NodeCache> nodeCacheMap = new ConcurrentHashMap<>();
    private CuratorFramework curatorFramework;

    private ZookeeperClient(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    public boolean isConnected() {
        return curatorFramework.getZookeeperClient().isConnected();
    }

    public void close() {
        closeNodeCache();

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

    public List<String> getChildren(String path) throws Exception {
        return curatorFramework.getChildren()
                .forPath(path);
    }

    public void listen(String path, NodeListener nodeListener) throws Exception {
        NodeCache nodeCache = new NodeCache(curatorFramework, path);

        nodeCache.getListenable().addListener(() -> {
            ChildData currentData = nodeCache.getCurrentData();

            if (nonNull(currentData)) {
                Node node = new Node.Builder()
                        .withStat(currentData.getStat())
                        .withValue(new String(currentData.getData()))
                        .build();

                nodeListener.nodeChanged(node);
            }
        });

        nodeCache.start();
        nodeCacheMap.put(path, nodeCache);
    }

    private void closeNodeCache() {
        nodeCacheMap.forEach((key, nodeCache) -> {
            try {
                nodeCache.close();
                LOGGER.debug("Close node cache for path /{}{}", NAMESPACE, key);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        });
    }

    public static class Builder {
        private int sessionTimeoutMs = DEFAULT_SESSION_TIMEOUT_MS;
        private int connectionTimeoutMs = DEFAULT_CONNECTION_TIMEOUT_MS;
        private boolean fastFail = DEFAULT_FAST_FAIL;
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

        public Builder fastFail(boolean fastFail) {
            this.fastFail = fastFail;
            return this;
        }

        public ZookeeperClient build() throws FirstConnectionTimeoutException {
            RetryPolicy retryPolicy = new RetryOneTime(SLEEP_MS_BETWEEN_RETRY);
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
            }

            if (notConnected(curatorFramework)) {
                if (fastFail) {
                    curatorFramework.close();
                    throw new FirstConnectionTimeoutException("Connection timeout when first time connect to zookeeper");
                }
                LOGGER.error("Cannot connect to zookeeper within 10 seconds, but will keep retry");
            }

            return new ZookeeperClient(curatorFramework);
        }

        private boolean notConnected(CuratorFramework curatorFramework) {
            return !curatorFramework.getZookeeperClient().isConnected();
        }
    }
}