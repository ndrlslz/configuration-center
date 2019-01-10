package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.api.json.application.CreateApplicationRequest;
import com.ndrlslz.configuration.center.api.json.application.CreateApplicationResponse;
import com.ndrlslz.configuration.center.api.json.application.GetApplicationsResponse;
import com.ndrlslz.configuration.center.api.service.ApplicationService;
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
public class ApplicationController {
    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("applications")
    @ResponseStatus(OK)
    @ApiOperation("Get Applications")
    public GetApplicationsResponse getApplications(@PageableDefault Pageable pageable) {
        return applicationService.getApplications(pageable);
    }

    @PostMapping("applications")
    @ResponseStatus(OK)
    @ApiOperation("Create Application")
    public CreateApplicationResponse createApplication(@RequestBody CreateApplicationRequest createApplicationRequest) {
        validate(createApplicationRequest);

        return applicationService.createApplication(createApplicationRequest);
    }

    @DeleteMapping("applications/{name}")
    @ResponseStatus(OK)
    @ApiOperation("Delete Application")
    public void deleteApplication(@PathVariable String name) {
        applicationService.deleteApplication(name);
    }
}
