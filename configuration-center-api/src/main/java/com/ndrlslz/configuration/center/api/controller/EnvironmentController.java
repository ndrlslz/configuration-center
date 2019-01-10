package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.api.json.environment.CreateEnvironmentRequest;
import com.ndrlslz.configuration.center.api.json.environment.CreateEnvironmentResponse;
import com.ndrlslz.configuration.center.api.json.environment.GetEnvironmentsResponse;
import com.ndrlslz.configuration.center.api.service.EnvironmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static com.ndrlslz.configuration.center.api.validation.EnvironmentDataValidator.validate;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class EnvironmentController {
    private final EnvironmentService environmentService;

    public EnvironmentController(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @GetMapping("applications/{application}/environments")
    @ResponseStatus(OK)
    @ApiOperation("Get specific application's Environments")
    public GetEnvironmentsResponse getApplications(@PageableDefault Pageable pageable, @PathVariable String application) {
        return environmentService.getEnvironments(application, pageable);
    }

    @PostMapping("applications/{application}/environments")
    @ResponseStatus(OK)
    @ApiOperation("Create specific application's Environment")
    public CreateEnvironmentResponse createEnvironment(@PathVariable String application,
                                                       @RequestBody CreateEnvironmentRequest createEnvironmentRequest) {
        validate(createEnvironmentRequest);

        return environmentService.createEnvironment(application, createEnvironmentRequest);
    }

    @DeleteMapping("applications/{application}/environments/{environment}")
    @ResponseStatus(OK)
    @ApiOperation("Delete specific application's environment")
    public void deleteEnvironment(@PathVariable String application, @PathVariable String environment) {
        environmentService.deleteEnvironment(application, environment);
    }
}
