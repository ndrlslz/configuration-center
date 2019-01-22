package com.ndrlslz.configuration.center.sdk.failover;

import com.ndrlslz.configuration.center.sdk.reader.ConfigurationFileReader;
import com.ndrlslz.configuration.center.sdk.storage.FailoverStorage;
import com.ndrlslz.configuration.center.sdk.storage.ZookeeperStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import static java.lang.String.valueOf;
import static java.nio.file.Files.exists;
import static java.nio.file.Paths.get;

public class ConfigurationFailover {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationFailover.class);
    public static final String MEMORY_CACHE_PATH = "configuration-center-failover/memory-cache/configuration.properties";
    private static final String DISASTER_RECOVERY_PATH = "configuration-center-failover/disaster-recovery/configuration.properties";
    private static volatile boolean FAILOVER = true;
    private ConfigurationFileReader configurationFileReader;

    public ConfigurationFailover() {
        this.configurationFileReader = new ConfigurationFileReader();
    }

    public void run() {
        if (FAILOVER) {
            synchronized (this) {
                if (FAILOVER) {
                    try {
                        clearStorage();
                        loadPropertiesIntoFailoverStorage();
                    } catch (IOException e) {
                        LOGGER.error("Failed to load properties into memory", e);
                    }
                    FAILOVER = false;
                }
            }
        }
    }

    private void clearStorage() {
        FailoverStorage.clear();
        ZookeeperStorage.clear();
    }

    private void loadPropertiesIntoFailoverStorage() throws IOException {
        Properties properties = loadProperties();

        properties
                .stringPropertyNames()
                .forEach(name -> {
                    FailoverStorage.set(name, valueOf(properties.get(name)));
                    LOGGER.info("Store {}={} into FailoverStorage", name, properties.getProperty(name));
                });
    }

    private Properties loadProperties() throws IOException {
        Path memoryCachePath = get(MEMORY_CACHE_PATH);
        Path disasterRecoveryPath = get(DISASTER_RECOVERY_PATH);

        if (exists(disasterRecoveryPath)) {
            LOGGER.info("load configurations from disaster recovery file");

            return configurationFileReader.read(disasterRecoveryPath);
        } else if (exists(memoryCachePath)) {
            LOGGER.info("load configurations from local memory cache file");

            return configurationFileReader.read(memoryCachePath);
        } else {
            LOGGER.warn("Cannot find either memory cache file or disaster recovery file");

            return new Properties();
        }
    }
}