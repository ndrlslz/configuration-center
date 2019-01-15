package com.ndrlslz.configuration.center.api.validation;

import com.ndrlslz.configuration.center.api.exception.InvalidRequestBodyException;
import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Type;
import com.ndrlslz.configuration.center.api.json.environment.CreateEnvironmentRequest;
import com.ndrlslz.configuration.center.api.json.environment.Environment;
import com.ndrlslz.configuration.center.api.util.DataBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EnvironmentDataValidatorTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private EnvironmentDataValidator validator = new EnvironmentDataValidator();

    @Test
    public void shouldValidateEnvironmentRequest() {
        CreateEnvironmentRequest createEnvironmentRequest = new CreateEnvironmentRequest();
        Data<Environment> data = new DataBuilder<Environment>()
                .withType(Type.ENVIRONMENT)
                .withAttributes(new Environment("dev"))
                .build();
        createEnvironmentRequest.setData(data);

        validator.validate(createEnvironmentRequest);
    }

    @Test
    public void shouldThrowExceptionGivenTypeNotMatch() {
        expectedException.expect(InvalidRequestBodyException.class);

        CreateEnvironmentRequest createEnvironmentRequest = new CreateEnvironmentRequest();
        Data<Environment> data = new DataBuilder<Environment>()
                .withType(Type.APPLICATION)
                .withAttributes(new Environment("dev"))
                .build();
        createEnvironmentRequest.setData(data);

        validator.validate(createEnvironmentRequest);
    }
}