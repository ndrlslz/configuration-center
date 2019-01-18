package com.ndrlslz.configuration.center.sdk.storage;

import java.util.concurrent.ConcurrentHashMap;

public class FailoverStorage {
    private static ConcurrentHashMap<String, String> configurationMap = new ConcurrentHashMap<>();

    public static String get(String name) {
        return configurationMap.get(name);
    }

    public static void set(String name, String value) {
        configurationMap.put(name, value);
    }
}
