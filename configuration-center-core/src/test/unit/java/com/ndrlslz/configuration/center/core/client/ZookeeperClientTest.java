package com.ndrlslz.configuration.center.core.client;

import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ZookeeperClientTest {
    private CuratorFramework curatorFramework;
    private ZookeeperClient zookeeperClient;

    @Before
    public void setUp() throws Exception {
        curatorFramework = mock(CuratorFramework.class);

        Constructor<ZookeeperClient> clientConstructor = ZookeeperClient.class.getDeclaredConstructor(CuratorFramework.class);
        clientConstructor.setAccessible(true);
        zookeeperClient = clientConstructor.newInstance(curatorFramework);
        clientConstructor.setAccessible(false);
    }

    @Test
    public void isConnected() {
        CuratorZookeeperClient curatorZookeeperClient = mock(CuratorZookeeperClient.class);

        when(curatorFramework.getZookeeperClient()).thenReturn(curatorZookeeperClient);
        when(curatorZookeeperClient.isConnected()).thenReturn(true);

        boolean connected = zookeeperClient.isConnected();
        assertThat(connected, is(true));
    }
}