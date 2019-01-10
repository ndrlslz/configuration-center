package com.ndrlslz.configuration.center.api.dao;

import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;

public interface EnvironmentDao {
    Page<String> getEnvironments(String application, Pagination pagination);

    void createEnvironment(String application, String environment);

    void deleteEnvironment(String application, String environment);
}
