package com.ndrlslz.configuration.center.sdk.storage;

import java.util.concurrent.ConcurrentHashMap;

public class MemoryStorage {
    private ConcurrentHashMap<String, String> configurationMap = new ConcurrentHashMap<>();

    public String get(String name) {
        return configurationMap.get(name);
    }

    public void set(String name, String value) {
        configurationMap.put(name, value);
    }
}
