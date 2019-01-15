package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.api.json.environment.CreateEnvironmentRequest;
import com.ndrlslz.configuration.center.api.json.environment.CreateEnvironmentResponse;
import com.ndrlslz.configuration.center.api.json.environment.GetEnvironmentsResponse;
import com.ndrlslz.configuration.center.api.service.EnvironmentService;
import com.ndrlslz.configuration.center.api.validation.EnvironmentDataValidator;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class EnvironmentController {
    private final EnvironmentService environmentService;
    private final EnvironmentDataValidator validator;

    public EnvironmentController(EnvironmentService environmentService, EnvironmentDataValidator validator) {
        this.environmentService = environmentService;
        this.validator = validator;
    }

    @GetMapping("/applications/{application}/environments")
    @ResponseStatus(OK)
    @ApiOperation("Get specific application's Environments")
    public GetEnvironmentsResponse getApplications(@PageableDefault Pageable pageable, @PathVariable String application) {
        return environmentService.getEnvironments(application, pageable);
    }

    @PostMapping("/applications/{application}/environments")
    @ResponseStatus(OK)
    @ApiOperation("Create specific application's Environment")
    public CreateEnvironmentResponse createEnvironment(@PathVariable String application,
                                                       @RequestBody CreateEnvironmentRequest createEnvironmentRequest) {
        validator.validate(createEnvironmentRequest);

        return environmentService.createEnvironment(application, createEnvironmentRequest);
    }

    @DeleteMapping("/applications/{application}/environments/{environment}")
    @ResponseStatus(OK)
    @ApiOperation("Delete specific application's environment")
    public void deleteEnvironment(@PathVariable String application, @PathVariable String environment) {
        environmentService.deleteEnvironment(application, environment);
    }
}
