package com.ndrlslz.configuration.center.api.dao;

import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;

public interface ConfigurationCenterDao {
    Page<String> getApplications(Pagination pagination);
}