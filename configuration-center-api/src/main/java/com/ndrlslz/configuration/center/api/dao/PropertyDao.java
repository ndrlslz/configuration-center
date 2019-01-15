package com.ndrlslz.configuration.center.api.dao;

import com.ndrlslz.configuration.center.core.model.Node;
import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;

public interface PropertyDao {
    Page<Node> getProperties(String application, String environment, Pagination pagination);

    void createProperty(String application, String environment, String property, String value);

    Node updateProperty(String application, String environment, String property, String value, int version);
}
