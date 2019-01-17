package com.ndrlslz.configuration.center.sdk.client;

import com.ndrlslz.configuration.center.sdk.exception.ConfigurationNotFoundException;
import com.ndrlslz.configuration.center.sdk.exception.ConfigurationSdkException;
import com.ndrlslz.configuration.center.sdk.failover.ConfigurationFailOver;
import com.ndrlslz.configuration.center.sdk.storage.MemoryStorage;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

abstract class ConfigurationAccessor implements ConfigurationOperations {
    ConfigurationAccessor() {
        ConfigurationFailOver configurationFailOver = new ConfigurationFailOver();
        configurationFailOver.failover();
    }

    abstract String getFromRemote(String name);

    @Override
    public String get(String name) {
        String valueFromRemote = getFromRemote(name);
        if (nonNull(valueFromRemote)) {
            MemoryStorage.set(name, valueFromRemote);
            return valueFromRemote;
        }

        String valueFromLocalMemory = MemoryStorage.get(name);
        if (isNull(valueFromLocalMemory)) {
            throw new ConfigurationNotFoundException(format("Cannot found property %s from either memory cache file or disaster recovery file, consider adding it into disaster recovery file", name));
        }
        return valueFromLocalMemory;
    }

    @Override
    public String get(String name, String defaultValue) {
        String value = null;
        try {
            value = get(name);
        } catch (ConfigurationSdkException ignored) {
        }

        return isNull(value) ? defaultValue : value;
    }
}
