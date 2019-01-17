package com.ndrlslz.configuration.center.sdk.failover;

import com.ndrlslz.configuration.center.sdk.reader.ConfigurationFileReader;
import com.ndrlslz.configuration.center.sdk.storage.MemoryStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigurationFailOver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationFailOver.class);
    private static final String MEMORY_CACHE_PATH = "configuration-center-failover/memory-cache/configurations.properties";
    private static final String DISASTER_RECOVERY_PATH = "configuration-center-failover/disaster-recovery/configurations.properties";
    private static volatile boolean FAILOVER = true;
    private ConfigurationFileReader configurationFileReader;

    public ConfigurationFailOver() {
        this.configurationFileReader = new ConfigurationFileReader();
    }

    public void failover() {
        if (FAILOVER) {
            synchronized (this) {
                if (FAILOVER) {
                    try {
                        load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FAILOVER = false;
                }
            }
        }
    }

    private void load() throws IOException {
        Path memoryCachePath = Paths.get(MEMORY_CACHE_PATH);
        Path disasterRecoveryPath = Paths.get(DISASTER_RECOVERY_PATH);

        Properties properties;
        if (Files.exists(memoryCachePath)) {
            LOGGER.info("load configurations from local memory cache file");
            properties = configurationFileReader.read(memoryCachePath);
        } else if (Files.exists(disasterRecoveryPath)) {
            LOGGER.info("load configurations from disaster recovery file");
            properties = configurationFileReader.read(disasterRecoveryPath);
        } else {
            LOGGER.warn("Cannot find either memory cache file or disaster recovery file");
            properties = new Properties();
        }

        properties
                .stringPropertyNames()
                .forEach(name -> MemoryStorage.set(name, String.valueOf(properties.get(name))));
    }
}
