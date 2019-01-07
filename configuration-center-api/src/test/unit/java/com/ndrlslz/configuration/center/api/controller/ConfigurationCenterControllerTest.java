package com.ndrlslz.configuration.center.api.controller;


import com.ndrlslz.configuration.center.api.json.GetApplicationsResponse;
import com.ndrlslz.configuration.center.api.service.ConfigurationCenterService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConfigurationCenterControllerTest {
    private final static PageRequest PAGE = new PageRequest(1, 10);

    @Mock
    private ConfigurationCenterService configurationCenterService;

    @InjectMocks
    private ConfigurationCenterController configurationCenterController;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldGetApplications() {
        GetApplicationsResponse expected = new GetApplicationsResponse();

        when(configurationCenterService.getApplications(PAGE)).thenReturn(expected);

        GetApplicationsResponse response = configurationCenterController.getApplications(PAGE);

        assertThat(response, is(expected));
    }
}
