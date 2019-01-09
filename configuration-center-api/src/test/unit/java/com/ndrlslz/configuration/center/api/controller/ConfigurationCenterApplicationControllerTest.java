package com.ndrlslz.configuration.center.api.controller;


import com.ndrlslz.configuration.center.api.exception.ConfigurationCenterWrapperException;
import com.ndrlslz.configuration.center.api.exception.InvalidRequestBodyException;
import com.ndrlslz.configuration.center.api.json.application.Application;
import com.ndrlslz.configuration.center.api.json.application.CreateApplicationRequest;
import com.ndrlslz.configuration.center.api.json.application.CreateApplicationResponse;
import com.ndrlslz.configuration.center.api.json.application.GetApplicationsResponse;
import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Type;
import com.ndrlslz.configuration.center.api.service.ConfigurationCenterService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConfigurationCenterApplicationControllerTest {
    private final static PageRequest PAGE = new PageRequest(1, 10);

    @Mock
    private ConfigurationCenterService configurationCenterService;

    @InjectMocks
    private ConfigurationCenterApplicationController configurationCenterApplicationController;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldGetApplications() {
        GetApplicationsResponse expected = new GetApplicationsResponse();

        when(configurationCenterService.getApplications(PAGE)).thenReturn(expected);

        GetApplicationsResponse response = configurationCenterApplicationController.getApplications(PAGE);

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
        when(configurationCenterService.createApplication(createApplicationRequest)).thenReturn(expected);

        CreateApplicationResponse response = configurationCenterApplicationController.createApplication(createApplicationRequest);

        assertThat(response, is(expected));
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyExceptionGivenTypeNotCorrect() {
        CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
        Data<Application> data = new Data<>();
        data.setType(Type.ENVIRONMENT);
        data.setAttributes(new Application("customer-api"));
        createApplicationRequest.setData(data);

        configurationCenterApplicationController.createApplication(createApplicationRequest);
    }

    @Test
    public void shouldDeleteApplication() {
        Mockito.doNothing().when(configurationCenterService).deleteApplication(any());

        configurationCenterApplicationController.deleteApplication("customer-api");
    }

    @Test(expected = ConfigurationCenterWrapperException.class)
    public void shouldThrowExceptionWhenDeleteApplication() {
        Mockito.doThrow(ConfigurationCenterWrapperException.class).when(configurationCenterService).deleteApplication(any());

        configurationCenterApplicationController.deleteApplication("customer-api");
    }
}
