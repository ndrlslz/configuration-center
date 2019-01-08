package com.ndrlslz.configuration.center.api.validation;

import com.ndrlslz.configuration.center.api.json.application.ApplicationRequest;

import static com.ndrlslz.configuration.center.api.validation.Validator.checkNotNull;

public class ApplicationDataValidator {
    public static void validate(ApplicationRequest applicationRequest) {
        checkNotNull(applicationRequest.getData(), "data cannot be null");
        checkNotNull(applicationRequest.getData().getAttributes(), "attribute cannot be null");
        checkNotNull(applicationRequest.getData().getAttributes().getName(), "application name is mandatory");
    }
}
