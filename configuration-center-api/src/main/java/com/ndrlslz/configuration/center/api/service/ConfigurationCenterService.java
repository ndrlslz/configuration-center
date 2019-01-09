package com.ndrlslz.configuration.center.api.service;

import com.ndrlslz.configuration.center.api.json.application.CreateApplicationRequest;
import com.ndrlslz.configuration.center.api.json.application.CreateApplicationResponse;
import com.ndrlslz.configuration.center.api.json.application.GetApplicationsResponse;
import org.springframework.data.domain.Pageable;


public interface ConfigurationCenterService {
    GetApplicationsResponse getApplications(Pageable pageable);

    CreateApplicationResponse createApplication(CreateApplicationRequest request);

    void deleteApplication(String application);
}
