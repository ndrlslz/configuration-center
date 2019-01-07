package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.api.json.GetApplicationsResponse;
import com.ndrlslz.configuration.center.api.service.ConfigurationCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigurationCenterController {
    private final ConfigurationCenterService configurationCenterService;

    @Autowired
    public ConfigurationCenterController(ConfigurationCenterService configurationCenterService) {
        this.configurationCenterService = configurationCenterService;
    }

    @GetMapping(path = "applications")
    public GetApplicationsResponse getApplications(@PageableDefault Pageable pageable) {
        return configurationCenterService.getApplications(pageable);
    }
}
