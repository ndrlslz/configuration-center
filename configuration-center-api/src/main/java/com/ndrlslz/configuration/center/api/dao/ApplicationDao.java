package com.ndrlslz.configuration.center.api.dao;

import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;

public interface ApplicationDao {
    Page<String> getApplications(Pagination pagination);

    void createApplication(String application);

    void deleteApplication(String application);
}
