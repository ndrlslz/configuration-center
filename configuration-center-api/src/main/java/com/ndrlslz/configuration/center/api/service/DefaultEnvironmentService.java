package com.ndrlslz.configuration.center.api.service;

import com.ndrlslz.configuration.center.api.dao.EnvironmentDao;
import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.environment.CreateEnvironmentRequest;
import com.ndrlslz.configuration.center.api.json.environment.CreateEnvironmentResponse;
import com.ndrlslz.configuration.center.api.json.environment.Environment;
import com.ndrlslz.configuration.center.api.json.environment.GetEnvironmentsResponse;
import com.ndrlslz.configuration.center.api.translator.EnvironmentDataTranslator;
import com.ndrlslz.configuration.center.api.util.LinksBuilder;
import com.ndrlslz.configuration.center.api.util.MetadataBuilder;
import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DefaultEnvironmentService implements EnvironmentService {
    private EnvironmentDao environmentDao;

    private EnvironmentDataTranslator environmentDataTranslator;

    @Autowired
    public DefaultEnvironmentService(EnvironmentDao environmentDao,
                                     EnvironmentDataTranslator environmentDataTranslator) {
        this.environmentDao = environmentDao;
        this.environmentDataTranslator = environmentDataTranslator;
    }

    @Override
    public GetEnvironmentsResponse getEnvironments(String application, Pageable pageable) {
        Pagination pagination = new Pagination.Builder()
                .withNumber(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .build();

        Page<String> environments = environmentDao.getEnvironments(application, pagination);

        GetEnvironmentsResponse getEnvironmentsResponse = new GetEnvironmentsResponse();
        getEnvironmentsResponse.setData(environmentDataTranslator.translate(environments.getContent(), application));
        getEnvironmentsResponse.setLinks(new LinksBuilder().withPage(environments).build());
        getEnvironmentsResponse.setMetadata(new MetadataBuilder().withPage(environments).build());

        return getEnvironmentsResponse;
    }

    @Override
    public CreateEnvironmentResponse createEnvironment(String application, CreateEnvironmentRequest createEnvironmentRequest) {
        String environment = createEnvironmentRequest.getData().getAttributes().getName();

        environmentDao.createEnvironment(application, environment);

        Data<Environment> data = environmentDataTranslator.transform(environment, application);
        CreateEnvironmentResponse createEnvironmentResponse = new CreateEnvironmentResponse();
        createEnvironmentResponse.setData(data);

        return createEnvironmentResponse;
    }

    @Override
    public void deleteEnvironment(String application, String environment) {
        environmentDao.deleteEnvironment(application, environment);
    }

}
