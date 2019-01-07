package com.ndrlslz.configuration.center.api.service;

import com.ndrlslz.configuration.center.api.dao.ConfigurationCenterDao;
import com.ndrlslz.configuration.center.api.json.GetApplicationsResponse;
import com.ndrlslz.configuration.center.api.translator.ApplicationDataTranslator;
import com.ndrlslz.configuration.center.api.util.LinksBuilder;
import com.ndrlslz.configuration.center.api.util.MetadataBuilder;
import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class DefaultConfigurationCenterService implements ConfigurationCenterService {
    private final ConfigurationCenterDao configurationCenterDao;

    private final ApplicationDataTranslator applicationDataTranslator;

    @Autowired
    public DefaultConfigurationCenterService(ConfigurationCenterDao dao,
                                             ApplicationDataTranslator applicationDataTranslator) {
        this.configurationCenterDao = dao;
        this.applicationDataTranslator = applicationDataTranslator;
    }

    @Override
    public GetApplicationsResponse getApplications(Pageable pageable) {
        Pagination pagination = new Pagination.Builder()
                .withSize(pageable.getPageSize())
                .withNumber(pageable.getPageNumber())
                .build();

        Page<String> applications = configurationCenterDao.getApplications(pagination);

        GetApplicationsResponse getApplicationsResponse = new GetApplicationsResponse();
        getApplicationsResponse.setData(applicationDataTranslator.translate(applications.getContent()));
        getApplicationsResponse.setLinks(new LinksBuilder().withPage(applications).build());
        getApplicationsResponse.setMetadata(new MetadataBuilder().withPage(applications).build());

        return getApplicationsResponse;
    }
}
