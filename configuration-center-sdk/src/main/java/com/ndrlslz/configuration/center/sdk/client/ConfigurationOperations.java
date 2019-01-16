package com.ndrlslz.configuration.center.sdk.client;

import com.ndrlslz.configuration.center.sdk.listener.ConfigListener;

public interface ConfigurationOperations {
    boolean isConnected();

    String get(String name);

    void listen(String name, ConfigListener configListener);

    void close();
}
