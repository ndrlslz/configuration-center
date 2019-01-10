package com.ndrlslz.configuration.center.api.service;

import com.ndrlslz.configuration.center.api.json.environment.CreateEnvironmentRequest;
import com.ndrlslz.configuration.center.api.json.environment.CreateEnvironmentResponse;
import com.ndrlslz.configuration.center.api.json.environment.GetEnvironmentsResponse;
import org.springframework.data.domain.Pageable;

public interface EnvironmentService {
    GetEnvironmentsResponse getEnvironments(String application, Pageable pageable);

    CreateEnvironmentResponse createEnvironment(String application, CreateEnvironmentRequest createEnvironmentRequest);

    void deleteEnvironment(String application, String environment);
}
