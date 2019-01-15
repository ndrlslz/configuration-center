package com.ndrlslz.configuration.center.api.validation;

import com.ndrlslz.configuration.center.api.exception.InvalidRequestBodyException;
import com.ndrlslz.configuration.center.api.json.application.Application;
import com.ndrlslz.configuration.center.api.json.application.CreateApplicationRequest;
import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Type;
import com.ndrlslz.configuration.center.api.util.DataBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ApplicationDataValidatorTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ApplicationDataValidator applicationDataValidator = new ApplicationDataValidator();

    @Test
    public void shouldValidateApplicationData() {
        CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
        Data<Application> data = new DataBuilder<Application>()
                .withType(Type.APPLICATION)
                .withAttributes(new Application("customer-api"))
                .build();
        createApplicationRequest.setData(data);

        applicationDataValidator.validate(createApplicationRequest);
    }

    @Test
    public void shouldThrowExceptionGivenTypeNotMatch() {
        expectedException.expect(InvalidRequestBodyException.class);
        expectedException.expectMessage("type not match, expect application, but receive PROPERTY");

        CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
        Data<Application> data = new DataBuilder<Application>()
                .withType(Type.PROPERTY)
                .withAttributes(new Application("customer-api"))
                .build();
        createApplicationRequest.setData(data);

        applicationDataValidator.validate(createApplicationRequest);
    }
}