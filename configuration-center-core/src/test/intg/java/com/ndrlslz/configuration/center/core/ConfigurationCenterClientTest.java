package com.ndrlslz.configuration.center.core;

import com.ndrlslz.configuration.center.core.client.ConfigurationCenterClient;
import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import com.ndrlslz.configuration.center.core.exception.FirstConnectionTimeoutException;
import com.ndrlslz.configuration.center.core.model.Node;
import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;
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

        Pagination pagination = new Pagination.Builder()
                .withSize(10)
                .withNumber(0)
                .build();

        List<String> applications = configurationCenterClient.getApplications(pagination).getContent();

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

        Pagination pagination = new Pagination.Builder()
                .withSize(10)
                .withNumber(0)
                .build();

        List<String> applications = configurationCenterClient.getApplications(pagination).getContent();

        assertThat(applications.size(), is(3));
        assertThat(applications, hasItems("customer-api", "product-api", "order-api"));
    }

    @Test
    public void shouldGetApplicationsByPagination() throws ConfigurationCenterException {
        configurationCenterClient.createApplication("customer-api");
        configurationCenterClient.createApplication("product-api");
        configurationCenterClient.createApplication("order-api");
        configurationCenterClient.createApplication("address-api");
        configurationCenterClient.createApplication("contract-api");

        Pagination firstPage = new Pagination.Builder()
                .withSize(2)
                .withNumber(0)
                .build();

        Page<String> applicationsPage = configurationCenterClient.getApplications(firstPage);
        List<String> applications = applicationsPage.getContent();

        assertThat(applicationsPage.getTotalPages(), is(3));
        assertThat(applicationsPage.getTotalElements(), is(5));
        assertThat(applications.size(), is(2));
        assertThat(applications, hasItems("customer-api", "product-api"));

        Pagination secondPage = new Pagination.Builder()
                .withSize(2)
                .withNumber(1)
                .build();

        List<String> secondApplications = configurationCenterClient.getApplications(secondPage).getContent();

        assertThat(secondApplications.size(), is(2));
        assertThat(secondApplications, hasItems("order-api", "address-api"));
    }

    @Test
    public void shouldThrowConnectionLossExceptionGivenZookeeperStopped() throws IOException, ConfigurationCenterException {
        expectedException.expect(ConfigurationCenterException.class);
        expectedException.expectCause(isA(KeeperException.ConnectionLossException.class));

        testingServer.close();

        Pagination pagination = new Pagination.Builder()
                .withSize(10)
                .withNumber(0)
                .build();
        configurationCenterClient.getApplications(pagination);
    }

    @Test
    public void shouldDeleteApplication() throws ConfigurationCenterException {
        configurationCenterClient.createApplication("customer-api");
        configurationCenterClient.createApplication("product-api");
        configurationCenterClient.createApplication("order-api");
        configurationCenterClient.deleteApplication("customer-api");

        Pagination pagination = new Pagination.Builder()
                .withSize(10)
                .withNumber(0)
                .build();

        List<String> applications = configurationCenterClient.getApplications(pagination).getContent();

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

        Pagination pagination = new Pagination.Builder()
                .withSize(10)
                .withNumber(0)
                .build();

        List<String> environments = configurationCenterClient.getEnvironments(CUSTOMER_API, pagination).getContent();

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
    public void shouldGetEnvironmentsByPagination() throws ConfigurationCenterException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, "dev");
        configurationCenterClient.createEnvironment(CUSTOMER_API, "integration");
        configurationCenterClient.createEnvironment(CUSTOMER_API, "uat");
        configurationCenterClient.createEnvironment(CUSTOMER_API, "performance");
        configurationCenterClient.createEnvironment(CUSTOMER_API, "prod");

        Pagination firstPage = new Pagination.Builder()
                .withSize(2)
                .withNumber(0)
                .build();

        Page<String> firstEnvironmentsPage = configurationCenterClient.getEnvironments(CUSTOMER_API, firstPage);
        List<String> firstEnvironments = firstEnvironmentsPage.getContent();

        assertThat(firstEnvironmentsPage.getTotalPages(), is(3));
        assertThat(firstEnvironmentsPage.getTotalElements(), is(5));
        assertThat(firstEnvironments.size(), is(2));

        Pagination pagination = new Pagination.Builder()
                .withSize(Integer.MAX_VALUE)
                .withNumber(0)
                .build();

        Page<String> environmentsPage = configurationCenterClient.getEnvironments(CUSTOMER_API, pagination);
        List<String> environments = environmentsPage.getContent();

        assertThat(environmentsPage.getTotalElements(), is(5));
        assertThat(environmentsPage.getTotalPages(), is(1));
        assertThat(environments.size(), is(5));
        assertThat(environments, hasItems("dev", "integration", "uat", "performance", "prod"));
    }

    @Test
    public void shouldThrowNoNodeExceptionGivenApplicationNotExists() throws ConfigurationCenterException {
        expectedException.expect(ConfigurationCenterException.class);
        expectedException.expectCause(isA(KeeperException.NoNodeException.class));

        Pagination pagination = new Pagination.Builder()
                .withSize(10)
                .withNumber(0)
                .build();

        configurationCenterClient.getEnvironments(CUSTOMER_API, pagination);
    }

    @Test
    public void shouldDeleteEnvironment() throws ConfigurationCenterException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, "dev");
        configurationCenterClient.createEnvironment(CUSTOMER_API, "uat");
        configurationCenterClient.createEnvironment(CUSTOMER_API, "prod");
        configurationCenterClient.deleteEnvironment(CUSTOMER_API, "prod");

        Pagination pagination = new Pagination.Builder()
                .withSize(10)
                .withNumber(0)
                .build();

        List<String> environments = configurationCenterClient.getEnvironments(CUSTOMER_API, pagination).getContent();

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

        assertThat(node.getName(), is("key"));
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
        List<String> names = nodes.stream().map(Node::getName).collect(toList());

        assertThat(values.size(), is(4));
        assertThat(values, hasItems("value1", "value2", "value3", "value4"));
        assertThat(names, hasItems("key1", "key2", "key3", "key4"));
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