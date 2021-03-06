package com.ndrlslz.configuration.center.sdk.client;

import com.ndrlslz.configuration.center.sdk.exception.ConfigurationNotFoundException;
import com.ndrlslz.configuration.center.sdk.exception.ConfigurationSdkException;
import com.ndrlslz.configuration.center.sdk.failover.ConfigurationFailover;
import com.ndrlslz.configuration.center.sdk.listener.ConfigurationListener;
import com.ndrlslz.configuration.center.sdk.model.ConfigListenerRecord;
import com.ndrlslz.configuration.center.sdk.persistence.ConfigurationPersistence;
import com.ndrlslz.configuration.center.sdk.storage.FailoverStorage;
import com.ndrlslz.configuration.center.sdk.storage.ZookeeperStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

abstract class ConfigurationAccessor implements ConfigurationOperations {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationAccessor.class);
    private static final Integer QUEUE_LENGTH = 100;
    private ConfigurationPersistence configurationPersistence;
    ArrayBlockingQueue<ConfigListenerRecord> configListenerRecordsQueue;

    ConfigurationAccessor() {
        new ConfigurationFailover().run();
        configurationPersistence = new ConfigurationPersistence();
        configurationPersistence.run();
        configListenerRecordsQueue = new ArrayBlockingQueue<>(QUEUE_LENGTH);
    }

    abstract String getFromRemote(String name);

    abstract void listenRemote(Object object, String name, ConfigurationListener configurationListener);

    abstract void closeRemote();

    abstract void unListenRemote(Object object, String name);

    @Override
    public String get(String name) {
        String valueFromRemote = getFromRemote(name);
        if (nonNull(valueFromRemote)) {
            ZookeeperStorage.set(name, valueFromRemote);
            return valueFromRemote;
        }

        LOGGER.debug("Cannot get {} from remote, failover to memory cache or disaster recovery", name);

        String valueFromFailover = FailoverStorage.get(name);
        if (nonNull(valueFromFailover)) {
            ZookeeperStorage.setIfAbsent(name, valueFromFailover);
            return valueFromFailover;
        }

        throw new ConfigurationNotFoundException(format("cannot connect zookeeper, and cannot found property %s from either memory cache file or disaster recovery file, consider adding it into disaster recovery file", name));
    }

    @Override
    public void listen(Object object, String name, ConfigurationListener configurationListener) {
        if (isConnected()) {
            listenRemote(object, name, configurationListener);
        } else {
            String valueFromFailover = FailoverStorage.get(name);
            if (isNull(valueFromFailover)) {
                throw new ConfigurationNotFoundException(format("Cannot connect zookeeper, and cannot found property %s from either memory cache file or disaster recovery file, consider adding it into disaster recovery file", name));
            }

            ZookeeperStorage.setIfAbsent(name, valueFromFailover);
            configurationListener.configChanged(valueFromFailover);

            ConfigListenerRecord record = new ConfigListenerRecord(object, name, configurationListener);
            configListenerRecordsQueue.offer(record);
        }
    }

    @Override
    public void unListen(Object object, String name) {
        unListenRemote(object, name);
    }

    @Override
    public String get(String name, String defaultValue) {
        String value = null;
        try {
            value = get(name);
        } catch (ConfigurationSdkException ignored) {
            LOGGER.debug("Cannot get {} from remote or memory cache file or disaster recovery file, fallback to default value: {}", name, defaultValue);
        }

        return isNull(value) ? defaultValue : value;
    }

    @Override
    public void close() {
        configurationPersistence.stop();

        closeRemote();
    }
}