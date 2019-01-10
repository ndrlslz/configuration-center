package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.api.exception.ConfigurationCenterWrapperException;
import com.ndrlslz.configuration.center.api.exception.InvalidRequestBodyException;
import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Type;
import com.ndrlslz.configuration.center.api.json.environment.CreateEnvironmentRequest;
import com.ndrlslz.configuration.center.api.json.environment.CreateEnvironmentResponse;
import com.ndrlslz.configuration.center.api.json.environment.Environment;
import com.ndrlslz.configuration.center.api.json.environment.GetEnvironmentsResponse;
import com.ndrlslz.configuration.center.api.service.EnvironmentService;
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

public class EnvironmentControllerTest {
    private final static PageRequest PAGE = new PageRequest(1, 10);

    @Mock
    private EnvironmentService service;

    @InjectMocks
    private EnvironmentController controller;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldGetEnvironments() {
        GetEnvironmentsResponse expected = new GetEnvironmentsResponse();

        when(service.getEnvironments("customer-api", PAGE)).thenReturn(expected);

        GetEnvironmentsResponse response = controller.getApplications(PAGE, "customer-api");

        assertThat(response, is(expected));
    }

    @Test
    public void shouldCreateEnvironment() {
        CreateEnvironmentRequest createEnvironmentRequest = new CreateEnvironmentRequest();
        Data<Environment> data = new Data<>();
        data.setType(Type.ENVIRONMENT);
        data.setAttributes(new Environment("dev"));
        createEnvironmentRequest.setData(data);

        CreateEnvironmentResponse expected = new CreateEnvironmentResponse();
        when(service.createEnvironment("customer-api", createEnvironmentRequest)).thenReturn(expected);

        CreateEnvironmentResponse response = controller.createEnvironment("customer-api", createEnvironmentRequest);

        assertThat(response, is(expected));
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowInvalidRequestBodyExceptionGivenTypeNotCorrect() {
        CreateEnvironmentRequest createEnvironmentRequest = new CreateEnvironmentRequest();
        Data<Environment> data = new Data<>();
        data.setType(Type.PROPERTY);
        data.setAttributes(new Environment("dev"));
        createEnvironmentRequest.setData(data);

        controller.createEnvironment("customer-api", createEnvironmentRequest);
    }

    @Test
    public void shouldDeleteEnvironment() {
        Mockito.doNothing().when(service).deleteEnvironment(any(), any());

        controller.deleteEnvironment("customer-api", "dev");
    }

    @Test(expected = ConfigurationCenterWrapperException.class)
    public void shouldThrowExceptionWhenDeleteEnvironment() {
        Mockito.doThrow(ConfigurationCenterWrapperException.class).when(service).deleteEnvironment(any(), any());

        controller.deleteEnvironment("customer-api", "dev");
    }
}