package com.ndrlslz.configuration.center.core.listener;

import com.ndrlslz.configuration.center.core.model.Node;

@FunctionalInterface
public interface NodeListener {
    void nodeChanged(Node node);
}
