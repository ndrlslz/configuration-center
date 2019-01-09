package com.ndrlslz.configuration.center.api.validation;

import com.ndrlslz.configuration.center.api.json.application.Application;
import com.ndrlslz.configuration.center.api.json.application.ApplicationRequest;
import com.ndrlslz.configuration.center.api.json.common.Data;

import static com.ndrlslz.configuration.center.api.json.common.Type.APPLICATION;
import static com.ndrlslz.configuration.center.api.validation.Validator.checkNotNull;
import static com.ndrlslz.configuration.center.api.validation.Validator.checkState;

public class ApplicationDataValidator {
    public static void validate(ApplicationRequest applicationRequest) {
        Data<Application> data = applicationRequest.getData();

        checkNotNull(data, "data cannot be null");
        checkNotNull(data.getAttributes(), "attribute cannot be null");
        checkNotNull(data.getAttributes().getName(), "application name is mandatory");
        checkState(data.getType() == APPLICATION, "type not match, expect application, but receive " + data.getType());
    }
}
