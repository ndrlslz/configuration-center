package com.ndrlslz.configuration.center.sdk.persistence;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ndrlslz.configuration.center.sdk.writer.ConfigurationFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ndrlslz.configuration.center.sdk.failover.ConfigurationFailover.MEMORY_CACHE_PATH;
import static java.lang.String.format;
import static java.nio.file.Paths.get;

public class ConfigurationPersistence {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationPersistence.class);
    private static final String CONFIG_FILE_NAME_FORMAT = "%s.%s";
    private ConfigurationFileWriter configurationFileWriter;
    private ScheduledExecutorService scheduledExecutorService;
    private AtomicInteger configFileTag = new AtomicInteger();


    public ConfigurationPersistence() {
        this.configurationFileWriter = new ConfigurationFileWriter();
        this.scheduledExecutorService = Executors
                .newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setDaemon(true).build());
    }

    public void run() {
        scheduledExecutorService
                .scheduleWithFixedDelay(this::persistConfigurations, 1, 1, TimeUnit.MINUTES);
    }

    public void stop() {
        scheduledExecutorService.shutdownNow();
    }

    private void persistConfigurations() {
        LOGGER.debug("start to persist the configurations from memory cache");

        Path configPath = get(MEMORY_CACHE_PATH);
        Path newConfigPath = get(format(CONFIG_FILE_NAME_FORMAT, MEMORY_CACHE_PATH, configFileTag.get()));

        try {
            configurationFileWriter.write(newConfigPath);

            Files.deleteIfExists(configPath);
            Files.move(newConfigPath, configPath);
            configFileTag.incrementAndGet();
        } catch (IOException e) {
            LOGGER.error("Failed to persist the configurations", e);
        }
    }
}