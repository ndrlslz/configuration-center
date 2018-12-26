package com.ndrlslz.configuration.center.core.client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationCenterClientTest {
    private ConfigurationCenterClient configurationCenterClient;
    private ZookeeperClient zookeeperClient;

    @Before
    public void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        zookeeperClient = Mockito.mock(ZookeeperClient.class);
        Constructor<ConfigurationCenterClient> constructor = ConfigurationCenterClient.class.getDeclaredConstructor(ZookeeperClient.class);

        constructor.setAccessible(true);
        configurationCenterClient = constructor.newInstance(zookeeperClient);
        constructor.setAccessible(false);
    }

    @Test
    public void isConnected() {
        Mockito.when(zookeeperClient.isConnected()).thenReturn(true);

        assertThat(configurationCenterClient.isConnected(), is(true));
    }
}