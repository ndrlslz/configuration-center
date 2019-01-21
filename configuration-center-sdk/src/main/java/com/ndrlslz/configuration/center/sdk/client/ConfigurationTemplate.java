package com.ndrlslz.configuration.center.sdk.client;

import com.ndrlslz.configuration.center.core.client.ConfigurationCenterClient;
import com.ndrlslz.configuration.center.core.client.ZookeeperClient;
import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import com.ndrlslz.configuration.center.core.exception.FirstConnectionTimeoutException;
import com.ndrlslz.configuration.center.sdk.exception.ZookeeperNodeNotExistsException;
import com.ndrlslz.configuration.center.sdk.listener.ConfigurationListener;
import com.ndrlslz.configuration.center.sdk.storage.ZookeeperStorage;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class ConfigurationTemplate extends ConfigurationAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationTemplate.class);
    private ConfigurationCenterClient configurationCenterClient;
    private String application;
    private String environment;

    private ConfigurationTemplate(Builder builder) {
        this.application = builder.application;
        this.environment = builder.environment;
        this.configurationCenterClient = builder.configurationCenterClient;
    }

    @Override
    public boolean isConnected() {
        return configurationCenterClient.isConnected();
    }

    @Override
    String getFromRemote(String name) {
        if (isConnected()) {
            try {
                return configurationCenterClient.getProperty(application, environment, name).getValue();
            } catch (ConfigurationCenterException e) {
                if (e.getCause() instanceof NoNodeException) {
                    throw new ZookeeperNodeNotExistsException(format("application is able to connect zookeeper, but cannot find node /%s/%s/%s/%s, consider adding it into zookeeper", ZookeeperClient.NAMESPACE, application, environment, name), e.getCause());
                }
                LOGGER.error("application is able to connect zookeeper, but cannot get node", e.getCause());
            }
        }
        return null;
    }

    @Override
    void listenRemote(String name, ConfigurationListener configurationListener) {
        try {
            configurationCenterClient.listenProperty(application, environment, name, node -> {
                configurationListener.configChanged(node.getValue());
                ZookeeperStorage.set(name, node.getValue());
            });
        } catch (ConfigurationCenterException e) {
            LOGGER.error(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void closeRemote() {
        configurationCenterClient.close();
    }

    public static class Builder {
        private String application;
        private String environment;
        private String connectionString;
        private Integer sessionTimeoutMs;
        private Integer connectionTimeoutMs;
        private ConfigurationCenterClient configurationCenterClient;

        public Builder application(String application) {
            this.application = application;
            return this;
        }

        public Builder environment(String environment) {
            this.environment = environment;
            return this;
        }

        public Builder connectionString(String connectionString) {
            this.connectionString = connectionString;
            return this;
        }

        public Builder sessionTimeoutMs(Integer sessionTimeoutMs) {
            this.sessionTimeoutMs = sessionTimeoutMs;
            return this;
        }

        public Builder connectionTimeoutMs(Integer connectionTimeoutMs) {
            this.connectionTimeoutMs = connectionTimeoutMs;
            return this;
        }

        public ConfigurationTemplate build() {
            requireNonNull(connectionString, "connectionString cannot be null");
            requireNonNull(application, "application cannot be null");
            requireNonNull(environment, "environment cannot be null");

            try {
                configurationCenterClient = new ConfigurationCenterClient.Builder()
                        .connectionString(connectionString)
                        .connectionTimeoutMs(connectionTimeoutMs)
                        .sessionTimeoutMs(sessionTimeoutMs)
                        .fastFail(false)
                        .build();
            } catch (FirstConnectionTimeoutException ignored) {
            }

            return new ConfigurationTemplate(this);
        }
    }
}
