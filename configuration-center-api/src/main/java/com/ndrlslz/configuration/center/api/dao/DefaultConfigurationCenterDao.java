package com.ndrlslz.configuration.center.api.dao;

import com.ndrlslz.configuration.center.api.exception.ConfigurationCenterWrapperException;
import com.ndrlslz.configuration.center.core.client.ConfigurationCenterClient;
import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultConfigurationCenterDao implements ConfigurationCenterDao {
    private final ConfigurationCenterClient configurationCenterClient;

    @Autowired
    public DefaultConfigurationCenterDao(ConfigurationCenterClient configurationCenterClient) {
        this.configurationCenterClient = configurationCenterClient;
    }

    @Override
    public Page<String> getApplications(Pagination pagination) {
        try {
            return configurationCenterClient.getApplications(pagination);
        } catch (ConfigurationCenterException e) {
            throw new ConfigurationCenterWrapperException(e.getMessage(), e);
        }
    }

    @Override
    public void createApplication(String application) {
        try {
            configurationCenterClient.createApplication(application);
        } catch (ConfigurationCenterException e) {
            throw new ConfigurationCenterWrapperException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteApplication(String application) {
        try {
            configurationCenterClient.deleteApplication(application);
        } catch (ConfigurationCenterException e) {
            throw new ConfigurationCenterWrapperException(e.getMessage(), e);
        }
    }
}