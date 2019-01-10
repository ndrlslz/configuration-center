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

public class DefaultEnvironmentDaoTest {
    private static final ConfigurationCenterException EXCEPTION = new ConfigurationCenterException("");
    private static final Pagination PAGE = new Pagination.Builder().withSize(10).withNumber(0).build();

    @Mock
    private ConfigurationCenterClient client;

    @InjectMocks
    private DefaultEnvironmentDao dao;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldGetEnvironments() throws ConfigurationCenterException {
        Page<String> expected = new Page.Builder<String>().build();

        Mockito.when(client.getEnvironments(any(), any())).thenReturn(expected);

        Page<String> environments = dao.getEnvironments("customer-api", PAGE);

        assertThat(environments, is(expected));
    }

    @Test(expected = ConfigurationCenterWrapperException.class)
    public void shouldThrowExceptionWhenGetEnvironments() throws ConfigurationCenterException {
        when(client.getEnvironments(any(), any())).thenThrow(EXCEPTION);

        dao.getEnvironments("customer-api", PAGE);
    }

    @Test
    public void shouldCreateEnvironment() throws ConfigurationCenterException {
        Mockito.doNothing().when(client).createEnvironment("customer-api", "dev");

        dao.createEnvironment("customer-api", "dev");
    }

    @Test(expected = ConfigurationCenterWrapperException.class)
    public void shouldThrowExceptionWhenCreateEnvironment() throws ConfigurationCenterException {
        Mockito.doThrow(EXCEPTION).when(client).createEnvironment("customer-api", "dev");

        dao.createEnvironment("customer-api", "dev");
    }

    @Test
    public void shouldDeleteEnvironment() throws ConfigurationCenterException {
        Mockito.doNothing().when(client).deleteEnvironment("customer-api", "dev");

        dao.deleteEnvironment("customer-api", "dev");
    }

    @Test(expected = ConfigurationCenterWrapperException.class)
    public void shouldThrowExceptionWhenDeleteEnvironment() throws ConfigurationCenterException {
        Mockito.doThrow(EXCEPTION).when(client).deleteEnvironment("customer-api", "dev");

        dao.deleteEnvironment("customer-api", "dev");
    }
}