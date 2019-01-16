package com.ndrlslz.configuration.center.sdk.client;

import com.ndrlslz.configuration.center.sdk.storage.MemoryStorage;

import static java.util.Objects.nonNull;

abstract class ConfigurationAccessor implements ConfigurationOperations {
    private MemoryStorage memoryStorage = new MemoryStorage();

    abstract String getFromZookeeper(String name);

    public String get(String name) {
        String valueFromZookeeper = getFromZookeeper(name);
        if (nonNull(valueFromZookeeper)) {
            memoryStorage.set(name, valueFromZookeeper);
            return valueFromZookeeper;
        }

        //    if custom file exists:
        //        load custom file into memory
        //    else if memory storage file exists:
        //        load memory storage file into memory
        //    else:
        //        throw exception.
        return null;
    }
}
