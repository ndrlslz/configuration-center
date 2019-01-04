package com.ndrlslz.configuration.center.api.service;

import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;
import org.springframework.data.domain.Pageable;


public interface ConfigurationCenterService {
    Page<String> getApplications(Pageable pageable);
}
