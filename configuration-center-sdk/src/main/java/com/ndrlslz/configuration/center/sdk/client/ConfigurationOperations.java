package com.ndrlslz.configuration.center.sdk.client;

import com.ndrlslz.configuration.center.sdk.listener.ConfigurationListener;

public interface ConfigurationOperations {
    boolean isConnected();

    String get(String name, String defaultValue);

    String get(String name);

    void listen(String name, ConfigurationListener configurationListener);

    void close();

    void unListen(String name);
}
