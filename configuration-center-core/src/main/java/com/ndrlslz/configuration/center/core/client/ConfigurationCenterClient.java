package com.ndrlslz.configuration.center.core.client;

import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import com.ndrlslz.configuration.center.core.exception.FirstConnectionTimeoutException;
import com.ndrlslz.configuration.center.core.listener.NodeListener;
import com.ndrlslz.configuration.center.core.model.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

import static com.ndrlslz.configuration.center.core.util.PathBuilder.pathOf;
import static com.ndrlslz.configuration.center.core.util.PathBuilder.pathOfRoot;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class ConfigurationCenterClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationCenterClient.class);
    private ZookeeperClient zookeeperClient;

    private ConfigurationCenterClient(ZookeeperClient zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
    }

    public boolean isConnected() {
        return zookeeperClient.isConnected();
    }

    public void close() {
        zookeeperClient.close();
    }

    public void createApplication(String application) throws ConfigurationCenterException {
        try {
            zookeeperClient.createNode(pathOf(application));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ConfigurationCenterException(e.getMessage(), e);
        }
    }

    public void deleteApplication(String application) throws ConfigurationCenterException {
        try {
            zookeeperClient.deleteNode(pathOf(application));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ConfigurationCenterException(e.getMessage(), e);
        }
    }

    public List<String> getApplications() throws ConfigurationCenterException {
        try {
            return zookeeperClient.getChildren(pathOfRoot());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ConfigurationCenterException(e.getMessage(), e);
        }
    }

    public void createEnvironment(String application, String environment) throws ConfigurationCenterException {
        try {
            zookeeperClient.createNode(pathOf(application, environment));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ConfigurationCenterException(e.getMessage(), e);
        }
    }

    public void deleteEnvironment(String application, String environment) throws ConfigurationCenterException {
        try {
            zookeeperClient.deleteNode(pathOf(application, environment));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ConfigurationCenterException(e.getMessage(), e);
        }
    }

    public List<String> getEnvironments(String application) throws ConfigurationCenterException {
        try {
            return zookeeperClient.getChildren(pathOf(application));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ConfigurationCenterException(e.getMessage(), e);
        }
    }

    public Node getProperty(String application, String environment, String property) throws ConfigurationCenterException {
        try {
            return zookeeperClient.getNode(pathOf(application, environment, property));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ConfigurationCenterException(e.getMessage(), e);
        }
    }

    public void createProperty(String application, String environment, String property, String value) throws ConfigurationCenterException {
        try {
            zookeeperClient.createNode(pathOf(application, environment, property), value);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ConfigurationCenterException(e.getMessage(), e);
        }
    }

    public Node updateProperty(String application, String environment, String property, String value, int version) throws ConfigurationCenterException {
        try {
            return zookeeperClient.updateNode(pathOf(application, environment, property), value, version);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ConfigurationCenterException(e.getMessage(), e);
        }
    }

    public void deleteProperty(String application, String environment, String property) throws ConfigurationCenterException {
        try {
            zookeeperClient.deleteNode(pathOf(application, environment, property));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ConfigurationCenterException(e.getMessage(), e);
        }
    }

    //TODO: currently use sync & block method to get properties. plan to use RxJava to rewrite this function.
    public List<Node> getProperties(String application, String environment) throws ConfigurationCenterException {
        try {
            List<String> children = zookeeperClient.getChildren(pathOf(application, environment));
            return children
                    .parallelStream()
                    .map(subPath -> {
                        try {
                            return getProperty(application, environment, subPath);
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(toList());
        } catch (Exception e) {
            throw new ConfigurationCenterException(e.getMessage(), e);
        }
    }

    public void listenProperty(String application, String environment, String property, NodeListener nodeListener) throws ConfigurationCenterException {
        try {
            zookeeperClient.listen(pathOf(application, environment, property), nodeListener);
        } catch (Exception e) {
            throw new ConfigurationCenterException(e.getMessage(), e);
        }
    }

    public static class Builder {
        private String connectionString;
        private Integer sessionTimeoutMs;
        private Integer connectionTimeoutMs;

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

        public ConfigurationCenterClient build() throws FirstConnectionTimeoutException {
            requireNonNull(connectionString, "connectionString cannot be null");

            ZookeeperClient.Builder builder = new ZookeeperClient.Builder()
                    .connectionString(connectionString);

            if (nonNull(sessionTimeoutMs)) {
                builder.sessionTimeoutMs(sessionTimeoutMs);
            }

            if (nonNull(connectionTimeoutMs)) {
                builder.connectionTimeoutMs(connectionTimeoutMs);
            }

            ZookeeperClient zookeeperClient = builder.build();
            return new ConfigurationCenterClient(zookeeperClient);
        }
    }
}
