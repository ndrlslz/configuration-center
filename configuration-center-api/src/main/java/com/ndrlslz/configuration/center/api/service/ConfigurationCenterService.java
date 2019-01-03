package com.ndrlslz.configuration.center.api.service;

import com.ndrlslz.configuration.center.api.dao.ConfigurationCenterDao;
import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationCenterService {
    private final ConfigurationCenterDao configurationCenterDao;

    @Autowired
    public ConfigurationCenterService(ConfigurationCenterDao dao) {
        this.configurationCenterDao = dao;
    }

    public Page<String> getApplications(Pagination pagination) {
        return configurationCenterDao.getApplications(pagination);
    }
}
