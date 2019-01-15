package com.ndrlslz.configuration.center.api.dao;

import com.ndrlslz.configuration.center.api.exception.ConfigurationCenterWrapperException;
import com.ndrlslz.configuration.center.core.client.ConfigurationCenterClient;
import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import com.ndrlslz.configuration.center.core.model.Node;
import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultPropertyDaoTest {
    private static final ConfigurationCenterException EXCEPTION = new ConfigurationCenterException("");
    private static final Pagination PAGE = new Pagination.Builder().withSize(10).withNumber(0).build();

    @Mock
    private ConfigurationCenterClient client;

    @InjectMocks
    private DefaultPropertyDao dao;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldGetProperties() throws ConfigurationCenterException {
        Page<Node> expected = new Page.Builder<Node>().build();

        when(client.getProperties(any(), any(), any())).thenReturn(expected);

        Page<Node> properties = dao.getProperties("customer-api", "dev", PAGE);

        assertThat(properties, is(expected));
    }

    @Test(expected = ConfigurationCenterWrapperException.class)
    public void shouldThrowExceptionWhenGetProperties() throws ConfigurationCenterException {
        when(client.getProperties(any(), any(), any())).thenThrow(EXCEPTION);

        dao.getProperties("customer-api", "dev", PAGE);
    }

    @Test
    public void shouldCreateProperty() throws ConfigurationCenterException {
        Mockito.doNothing().when(client).createProperty("customer-api", "dev", "key", "value");

        dao.createProperty("customer-api", "dev", "key", "value");
    }

    @Test(expected = ConfigurationCenterWrapperException.class)
    public void shouldThrowExceptionWhenCreateProperty() throws ConfigurationCenterException {
        Mockito.doThrow(EXCEPTION).when(client).createProperty("customer-api", "dev", "key", "value");

        dao.createProperty("customer-api", "dev", "key", "value");
    }

    @Test
    public void shouldUpdateProperty() throws ConfigurationCenterException {
        Node expected = new Node.Builder()
                .withValue("value")
                .withStat(new Stat())
                .build();
        expected.setName("key");

        when(client.updateProperty(any(), any(), any(), any(), anyInt())).thenReturn(expected);

        Node node = dao.updateProperty("customer-api", "dev", "key", "value", 1);

        assertThat(node, is(expected));
    }

    @Test(expected = ConfigurationCenterWrapperException.class)
    public void shouldThrowExceptionWhenUpdateProperty() throws ConfigurationCenterException {
        Mockito.doThrow(EXCEPTION).when(client).updateProperty(any(), any(), any(), any(), anyInt());

        dao.updateProperty("customer-api", "dev", "key", "value", 1);
    }

    @Test
    public void shouldDeleteProperty() throws ConfigurationCenterException {
        Mockito.doNothing().when(client).deleteProperty("customer-api", "dev", "key");

        dao.deleteProperty("customer-api", "dev", "key");
    }

    @Test(expected = ConfigurationCenterWrapperException.class)
    public void shouldThrowExceptionWhenDeleteProperty() throws ConfigurationCenterException {
        Mockito.doThrow(EXCEPTION).when(client).deleteProperty("customer-api", "dev", "key");

        dao.deleteProperty("customer-api", "dev", "key");
    }
}
