package com.ndrlslz.configuration.center.api.validation;

import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.environment.Environment;
import com.ndrlslz.configuration.center.api.json.environment.EnvironmentRequest;
import org.springframework.stereotype.Service;

import static com.ndrlslz.configuration.center.api.json.common.Type.ENVIRONMENT;

@Service
public class EnvironmentDataValidator implements Validator<EnvironmentRequest> {
    @Override
    public void validate(EnvironmentRequest environmentRequest) {
        Data<Environment> data = environmentRequest.getData();

        checkNotNull(data, "data cannot be null");
        checkNotNull(data.getAttributes(), "attribute cannot be null");
        checkNotNull(data.getAttributes().getName(), "environment name is mandatory");
        checkState(data.getType() == ENVIRONMENT, "type not match, expect environment, but receive " + data.getType());
    }
}
