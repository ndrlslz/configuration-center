package com.ndrlslz.configuration.center.spring.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

public class DisasterRecoveryFileBuilder {
    public static final Path DISASTER_RECOVERY_PATH = Paths.get("configuration-center-failover/disaster-recovery/configuration.properties");
    private HashMap<String, String> map;

    public DisasterRecoveryFileBuilder() {
        this.map = new HashMap<>();
    }

    public DisasterRecoveryFileBuilder property(String name, String value) {
        map.put(name, value);
        return this;
    }

    public void create() throws IOException {
        Files.deleteIfExists(DISASTER_RECOVERY_PATH);
        Files.createDirectories(DISASTER_RECOVERY_PATH.getParent());
        Files.createFile(DISASTER_RECOVERY_PATH);

        StringBuilder stringBuilder = new StringBuilder();
        map.forEach((key, value) -> stringBuilder.append(key).append("=").append(value).append(System.lineSeparator()));

        Files.write(DISASTER_RECOVERY_PATH, stringBuilder.toString().getBytes(), StandardOpenOption.APPEND);
    }

}
