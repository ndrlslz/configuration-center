package com.ndrlslz.configuration.center.api.dao;

import com.ndrlslz.configuration.center.api.exception.ConfigurationCenterWrapperException;
import com.ndrlslz.configuration.center.core.client.ConfigurationCenterClient;
import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultApplicationDaoTest {
    private static final ConfigurationCenterException EXCEPTION = new ConfigurationCenterException("");
    private static final Pagination PAGE = new Pagination.Builder().withSize(10).withNumber(0).build();
    @Mock
    private ConfigurationCenterClient client;

    @InjectMocks
    private DefaultApplicationDao dao;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldGetApplications() throws ConfigurationCenterException {
        Page<String> expected = new Page.Builder<String>().build();

        when(client.getApplications(any())).thenReturn(expected);

        Page<String> applications = dao.getApplications(PAGE);

        assertThat(applications, is(expected));
    }

    @Test(expected = ConfigurationCenterWrapperException.class)
    public void shouldThrowExceptionWhenGetApplications() throws ConfigurationCenterException {
        when(client.getApplications(any())).thenThrow(EXCEPTION);

        dao.getApplications(PAGE);
    }

    @Test
    public void shouldCreateApplication() throws ConfigurationCenterException {
        Mockito.doNothing().when(client).createApplication("customer-api");

        dao.createApplication("customer-api");
    }

    @Test(expected = ConfigurationCenterWrapperException.class)
    public void shouldThrowExceptionWhenCreateApplication() throws ConfigurationCenterException {
        Mockito.doThrow(EXCEPTION).when(client).createApplication("customer-api");

        dao.createApplication("customer-api");
    }

    @Test
    public void shouldDeleteApplication() throws ConfigurationCenterException {
        Mockito.doNothing().when(client).deleteApplication("customer-api");

        dao.deleteApplication("customer-api");
    }

    @Test(expected = ConfigurationCenterWrapperException.class)
    public void shouldThrowExceptionWhenDeleteApplication() throws ConfigurationCenterException {
        Mockito.doThrow(EXCEPTION).when(client).deleteApplication("customer-api");

        dao.deleteApplication("customer-api");
    }
}

