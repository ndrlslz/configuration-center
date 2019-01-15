package com.ndrlslz.configuration.center.api.controller;


import com.ndrlslz.configuration.center.api.exception.ConfigurationCenterWrapperException;
import com.ndrlslz.configuration.center.api.exception.InvalidRequestBodyException;
import com.ndrlslz.configuration.center.api.json.application.Application;
import com.ndrlslz.configuration.center.api.json.application.CreateApplicationRequest;
import com.ndrlslz.configuration.center.api.json.application.CreateApplicationResponse;
import com.ndrlslz.configuration.center.api.json.application.GetApplicationsResponse;
import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Type;
import com.ndrlslz.configuration.center.api.service.ApplicationService;
import com.ndrlslz.configuration.center.api.validation.ApplicationDataValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ApplicationControllerTest {
    private final static PageRequest PAGE = new PageRequest(1, 10);

    @Mock
    private ApplicationService service;

    private ApplicationDataValidator validator = new ApplicationDataValidator();

    private ApplicationController controller;

    @Before
    public void setUp() {
        initMocks(this);
        controller = new ApplicationController(service, validator);
    }

    @Test
    public void shouldGetApplications() {
        GetApplicationsResponse expected = new GetApplicationsResponse();

        when(service.getApplications(PAGE)).thenReturn(expected);

        GetApplicationsResponse response = controller.getApplications(PAGE);

        assertThat(response, is(expected));
    }

    @Test
    public void shouldCreateApplication() {
        CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
        Data<Application> data = new Data<>();
        data.setType(Type.APPLICATION);
        data.setAttributes(new Application("customer-api"));
        createApplicationRequest.setData(data);

        CreateApplicationResponse expected = new CreateApplicationResponse();
        when(service.createApplication(createApplicationRequest)).thenReturn(expected);

        CreateApplicationResponse response = controller.createApplication(createApplicationRequest);

        assertThat(response, is(expected));
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyExceptionGivenTypeNotCorrect() {
        CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
        Data<Application> data = new Data<>();
        data.setType(Type.ENVIRONMENT);
        data.setAttributes(new Application("customer-api"));
        createApplicationRequest.setData(data);

        controller.createApplication(createApplicationRequest);
    }

    @Test
    public void shouldDeleteApplication() {
        Mockito.doNothing().when(service).deleteApplication(any());

        controller.deleteApplication("customer-api");
    }

    @Test(expected = ConfigurationCenterWrapperException.class)
    public void shouldThrowExceptionWhenDeleteApplication() {
        Mockito.doThrow(ConfigurationCenterWrapperException.class).when(service).deleteApplication(any());

        controller.deleteApplication("customer-api");
    }
}
