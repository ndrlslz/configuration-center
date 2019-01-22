package com.ndrlslz.configuration.center.sdk.storage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class ZookeeperStorage {
    private static ConcurrentHashMap<String, String> configurationMap = new ConcurrentHashMap<>();

    public static void set(String name, String value) {
        configurationMap.put(name, value);
    }

    public static void setIfAbsent(String name, String value) {
        configurationMap.putIfAbsent(name, value);
    }

    public static void forEach(BiConsumer<String, String> action) {
        configurationMap.forEach(action);
    }

    public static void clear() {
        configurationMap.clear();
    }
}
