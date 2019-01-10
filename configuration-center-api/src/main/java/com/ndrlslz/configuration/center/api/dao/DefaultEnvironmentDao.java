package com.ndrlslz.configuration.center.api.dao;

import com.ndrlslz.configuration.center.api.exception.ConfigurationCenterWrapperException;
import com.ndrlslz.configuration.center.core.client.ConfigurationCenterClient;
import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultEnvironmentDao implements EnvironmentDao {
    private ConfigurationCenterClient configurationCenterClient;

    @Autowired
    public DefaultEnvironmentDao(ConfigurationCenterClient configurationCenterClient) {
        this.configurationCenterClient = configurationCenterClient;
    }

    @Override
    public Page<String> getEnvironments(String application, Pagination pagination) {
        try {
            return configurationCenterClient.getEnvironments(application, pagination);
        } catch (ConfigurationCenterException e) {
            throw new ConfigurationCenterWrapperException(e.getMessage(), e);
        }
    }

    @Override
    public void createEnvironment(String application, String environment) {
        try {
            configurationCenterClient.createEnvironment(application, environment);
        } catch (ConfigurationCenterException e) {
            throw new ConfigurationCenterWrapperException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteEnvironment(String application, String environment) {
        try {
            configurationCenterClient.deleteEnvironment(application, environment);
        } catch (ConfigurationCenterException e) {
            throw new ConfigurationCenterWrapperException(e.getMessage(), e);
        }
    }
}
