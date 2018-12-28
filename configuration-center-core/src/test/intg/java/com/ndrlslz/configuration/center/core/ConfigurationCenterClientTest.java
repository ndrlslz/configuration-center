package com.ndrlslz.configuration.center.core;

import com.ndrlslz.configuration.center.core.client.ConfigurationCenterClient;
import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import com.ndrlslz.configuration.center.core.exception.FirstConnectionTimeoutException;
import com.ndrlslz.configuration.center.core.model.Node;
import org.apache.zookeeper.KeeperException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationCenterClientTest extends ConfigurationCenterBaseIntegrationTest {
    private static final String CUSTOMER_API = "customer-api";
    private static final String DEV = "dev";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldConnectConfigurationCenter() throws FirstConnectionTimeoutException {
        configurationCenterClient = new ConfigurationCenterClient.Builder()
                .connectionString(testingServer.getConnectString())
                .build();

        assertThat(configurationCenterClient.isConnected(), is(true));
    }

    @Test
    public void shouldCreateApplicationNode() throws ConfigurationCenterException {
        configurationCenterClient.createApplication(CUSTOMER_API);

        List<String> applications = configurationCenterClient.getApplications();

        assertThat(applications.size(), is(1));
        assertThat(applications, hasItem(CUSTOMER_API));
    }

    @Test
    public void shouldThrowNodeExistsExceptionGivenCreateApplicationTwice() throws ConfigurationCenterException {
        expectedException.expect(ConfigurationCenterException.class);
        expectedException.expectCause(isA(KeeperException.NodeExistsException.class));

        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createApplication(CUSTOMER_API);
    }

    @Test
    public void shouldGetApplications() throws ConfigurationCenterException {
        configurationCenterClient.createApplication("customer-api");
        configurationCenterClient.createApplication("product-api");
        configurationCenterClient.createApplication("order-api");

        List<String> applications = configurationCenterClient.getApplications();

        assertThat(applications.size(), is(3));
        assertThat(applications, hasItems("customer-api", "product-api", "order-api"));
    }

    @Test
    public void shouldThrowConnectionLossExceptionGivenZookeeperStopped() throws IOException, ConfigurationCenterException {
        expectedException.expect(ConfigurationCenterException.class);
        expectedException.expectCause(isA(KeeperException.ConnectionLossException.class));

        testingServer.close();
        configurationCenterClient.getApplications();
    }

    @Test
    public void shouldDeleteApplication() throws ConfigurationCenterException {
        configurationCenterClient.createApplication("customer-api");
        configurationCenterClient.createApplication("product-api");
        configurationCenterClient.createApplication("order-api");
        configurationCenterClient.deleteApplication("customer-api");

        List<String> applications = configurationCenterClient.getApplications();

        assertThat(applications.size(), is(2));
        assertThat(applications, hasItems("product-api", "order-api"));
    }

    @Test
    public void shouldThrowNoNodeExceptionGivenDeleteNotExistsApplication() throws ConfigurationCenterException {
        expectedException.expect(ConfigurationCenterException.class);
        expectedException.expectCause(isA(KeeperException.NoNodeException.class));

        configurationCenterClient.deleteApplication(CUSTOMER_API);
    }

    @Test
    public void shouldCreateEnvironment() throws ConfigurationCenterException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, "dev");

        List<String> environments = configurationCenterClient.getEnvironments(CUSTOMER_API);

        assertThat(environments.size(), is(1));
        assertThat(environments, hasItem("dev"));
    }

    @Test
    public void shouldThrowNodeExistsExceptionGivenCreateEnvironmentTwice() throws ConfigurationCenterException {
        expectedException.expect(ConfigurationCenterException.class);
        expectedException.expectCause(isA(KeeperException.NodeExistsException.class));

        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, "dev");
        configurationCenterClient.createEnvironment(CUSTOMER_API, "dev");
    }

    @Test
    public void shouldGetEnvironments() throws ConfigurationCenterException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, "dev");
        configurationCenterClient.createEnvironment(CUSTOMER_API, "uat");
        configurationCenterClient.createEnvironment(CUSTOMER_API, "prod");

        List<String> environments = configurationCenterClient.getEnvironments(CUSTOMER_API);

        assertThat(environments.size(), is(3));
        assertThat(environments, hasItems("dev", "uat", "prod"));
    }

    @Test
    public void shouldThrowNoNodeExceptionGivenApplicationNotExists() throws ConfigurationCenterException {
        expectedException.expect(ConfigurationCenterException.class);
        expectedException.expectCause(isA(KeeperException.NoNodeException.class));

        configurationCenterClient.getEnvironments(CUSTOMER_API);
    }

    @Test
    public void shouldDeleteEnvironment() throws ConfigurationCenterException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, "dev");
        configurationCenterClient.createEnvironment(CUSTOMER_API, "uat");
        configurationCenterClient.createEnvironment(CUSTOMER_API, "prod");
        configurationCenterClient.deleteEnvironment(CUSTOMER_API, "prod");

        List<String> environments = configurationCenterClient.getEnvironments(CUSTOMER_API);

        assertThat(environments.size(), is(2));
        assertThat(environments, hasItems("dev", "uat"));
    }

    @Test
    public void shouldThrowNoNodeExceptionGivenEnvironmentNotExists() throws ConfigurationCenterException {
        expectedException.expect(ConfigurationCenterException.class);
        expectedException.expectCause(isA(KeeperException.NoNodeException.class));

        configurationCenterClient.deleteEnvironment(CUSTOMER_API, "dev");
    }

    @Test
    public void shouldCreateAndGetProperty() throws ConfigurationCenterException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, DEV);
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key", "value");

        Node node = configurationCenterClient.getProperty(CUSTOMER_API, DEV, "key");

        assertThat(node.getValue(), is("value"));
    }

    @Test
    public void shouldThrowNodeExistsExceptionGivenCreatePropertyTwice() throws ConfigurationCenterException {
        expectedException.expect(ConfigurationCenterException.class);
        expectedException.expectCause(isA(KeeperException.NodeExistsException.class));
        expectedException.expectMessage("NodeExists for /configuration-center/customer-api/dev/key");

        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, DEV);
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key", "value");
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key", "value");
    }

    @Test
    public void shouldUpdateProperty() throws ConfigurationCenterException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, DEV);
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key", "value");

        Node node = configurationCenterClient.getProperty(CUSTOMER_API, DEV, "key");
        Node returnNode = configurationCenterClient.updateProperty(CUSTOMER_API, DEV, "key", "newValue", node.getVersion());

        Node newNode = configurationCenterClient.getProperty(CUSTOMER_API, DEV, "key");
        assertThat(newNode.getValue(), is("newValue"));
        assertThat(returnNode.getValue(), is("newValue"));
    }

    @Test
    public void shouldThrowBadVersionExceptionWhenUpdatePropertyGivenVersionNotMatch() throws ConfigurationCenterException {
        expectedException.expect(ConfigurationCenterException.class);
        expectedException.expectCause(isA(KeeperException.BadVersionException.class));

        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, DEV);
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key", "value");

        configurationCenterClient.updateProperty(CUSTOMER_API, DEV, "key", "newValue", 10);
    }

    @Test
    public void shouldDeleteProperty() throws ConfigurationCenterException {
        expectedException.expect(ConfigurationCenterException.class);
        expectedException.expectCause(isA(KeeperException.NoNodeException.class));

        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, DEV);
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key", "value");
        configurationCenterClient.deleteProperty(CUSTOMER_API, DEV, "key");

        configurationCenterClient.getProperty(CUSTOMER_API, DEV, "key");
    }

    @Test
    public void shouldThrowNoNodeExceptionWhenDeletePropertyGivenNodeNotExists() throws ConfigurationCenterException {
        expectedException.expect(ConfigurationCenterException.class);
        expectedException.expectCause(isA(KeeperException.NoNodeException.class));

        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, DEV);

        configurationCenterClient.deleteProperty(CUSTOMER_API, DEV, "key");
    }

    @Test
    public void shouldGetProperties() throws ConfigurationCenterException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, DEV);
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key1", "value1");
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key2", "value2");
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key3", "value3");
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key4", "value4");

        List<Node> nodes = configurationCenterClient.getProperties(CUSTOMER_API, DEV);
        List<String> values = nodes.stream().map(Node::getValue).collect(toList());

        assertThat(values.size(), is(4));
        assertThat(values, hasItems("value1", "value2", "value3", "value4"));
    }

    @Test
    public void shouldThrowNoNodeExceptionWhenGetPropertiesGivenEnvironmentNodeNotExists() throws ConfigurationCenterException {
        expectedException.expect(ConfigurationCenterException.class);
        expectedException.expectCause(isA(KeeperException.NoNodeException.class));

        configurationCenterClient.createApplication(CUSTOMER_API);

        configurationCenterClient.getProperties(CUSTOMER_API, DEV);
    }

    @Test
    public void shouldListenProperty() throws ConfigurationCenterException, InterruptedException {
        List<String> result = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(3);
        configurationCenterClient.listenProperty(CUSTOMER_API, DEV, "key", node -> {
            result.add(node.getValue());
            latch.countDown();
        });

        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, DEV);
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key", "value_one");
        TimeUnit.MILLISECONDS.sleep(100);
        configurationCenterClient.updateProperty(CUSTOMER_API, DEV, "key", "value_two",
                configurationCenterClient.getProperty(CUSTOMER_API, DEV, "key").getVersion());
        TimeUnit.MILLISECONDS.sleep(100);
        configurationCenterClient.updateProperty(CUSTOMER_API, DEV, "key", "value_three",
                configurationCenterClient.getProperty(CUSTOMER_API, DEV, "key").getVersion());

        latch.await();
        assertThat(result.size(), is(3));
        assertThat(result, hasItems("value_one", "value_two", "value_three"));
    }
}