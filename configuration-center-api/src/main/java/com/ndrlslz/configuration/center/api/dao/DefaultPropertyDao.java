package com.ndrlslz.configuration.center.api.dao;

import com.ndrlslz.configuration.center.api.exception.ConfigurationCenterWrapperException;
import com.ndrlslz.configuration.center.core.client.ConfigurationCenterClient;
import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import com.ndrlslz.configuration.center.core.model.Node;
import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultPropertyDao implements PropertyDao {
    private ConfigurationCenterClient configurationCenterClient;

    @Autowired
    public DefaultPropertyDao(ConfigurationCenterClient configurationCenterClient) {
        this.configurationCenterClient = configurationCenterClient;
    }

    @Override
    public Page<Node> getProperties(String application, String environment, Pagination pagination) {
        try {
            return configurationCenterClient.getProperties(application, environment, pagination);
        } catch (ConfigurationCenterException e) {
            throw new ConfigurationCenterWrapperException(e.getMessage(), e);
        }
    }

    @Override
    public void createProperty(String application, String environment, String property, String value) {
        try {
            configurationCenterClient.createProperty(application, environment, property, value);
        } catch (ConfigurationCenterException e) {
            throw new ConfigurationCenterWrapperException(e.getMessage(), e);
        }
    }

    @Override
    public Node updateProperty(String application, String environment, String property, String value, int version) {
        try {
            return configurationCenterClient.updateProperty(application, environment, property, value, version);
        } catch (ConfigurationCenterException e) {
            throw new ConfigurationCenterWrapperException(e.getMessage(), e);
        }
    }
}
