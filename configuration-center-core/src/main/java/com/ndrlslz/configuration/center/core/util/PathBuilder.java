package com.ndrlslz.configuration.center.core.util;

import com.google.common.base.Joiner;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class PathBuilder {
    private static final String DELIMITER = "/";

    public static String pathOf(String... path) {
        checkState(path.length > 0, "path cannot be empty");
        for (String p : path) {
            checkPath(p);
        }

        return DELIMITER + Joiner.on(DELIMITER).join(path);
    }

    public static String pathOfRoot() {
        return DELIMITER;
    }

    private static void checkPath(String path) {
        checkNotNull(path, "path cannot be null");
        checkState(!path.contains(DELIMITER), "%s cannot contains slash special char", path);
    }
}
