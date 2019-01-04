package com.ndrlslz.configuration.center.api.service;

import com.ndrlslz.configuration.center.api.dao.ConfigurationCenterDao;
import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class DefaultConfigurationCenterService implements ConfigurationCenterService {
    private final ConfigurationCenterDao configurationCenterDao;

    @Autowired
    public DefaultConfigurationCenterService(ConfigurationCenterDao dao) {
        this.configurationCenterDao = dao;
    }

    @Override
    public Page<String> getApplications(Pageable pageable) {
        Pagination pagination = new Pagination.Builder()
                .withSize(pageable.getPageSize())
                .withNumber(pageable.getPageNumber())
                .build();

        return configurationCenterDao.getApplications(pagination);
    }
}
