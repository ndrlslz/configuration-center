package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.api.json.application.CreateApplicationRequest;
import com.ndrlslz.configuration.center.api.json.application.CreateApplicationResponse;
import com.ndrlslz.configuration.center.api.json.application.GetApplicationsResponse;
import com.ndrlslz.configuration.center.api.service.ConfigurationCenterService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static com.ndrlslz.configuration.center.api.validation.ApplicationDataValidator.validate;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class ConfigurationCenterApplicationController {
    private final ConfigurationCenterService configurationCenterService;

    @Autowired
    public ConfigurationCenterApplicationController(ConfigurationCenterService configurationCenterService) {
        this.configurationCenterService = configurationCenterService;
    }

    @GetMapping(path = "applications")
    @ResponseStatus(OK)
    @ApiOperation("Get Applications")
    public GetApplicationsResponse getApplications(@PageableDefault Pageable pageable) {
        return configurationCenterService.getApplications(pageable);
    }

    @PostMapping(path = "applications")
    @ResponseStatus(OK)
    @ApiOperation("Create Application")
    public CreateApplicationResponse createApplication(@RequestBody CreateApplicationRequest createApplicationRequest) {
        validate(createApplicationRequest);

        return configurationCenterService.createApplication(createApplicationRequest);
    }
}
