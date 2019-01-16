package com.ndrlslz.configuration.center.core.client;

import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import com.ndrlslz.configuration.center.core.exception.FirstConnectionTimeoutException;
import com.ndrlslz.configuration.center.core.listener.NodeListener;
import com.ndrlslz.configuration.center.core.model.AsyncResult;
import com.ndrlslz.configuration.center.core.model.Node;
import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.ndrlslz.configuration.center.core.util.AsyncHelper.async;
import static com.ndrlslz.configuration.center.core.util.PaginationHelper.pagination;
import static com.ndrlslz.configuration.center.core.util.PathBuilder.pathOf;
import static com.ndrlslz.configuration.center.core.util.PathBuilder.pathOfRoot;
import static io.reactivex.schedulers.Schedulers.io;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class ConfigurationCenterClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationCenterClient.class);
    private ZookeeperClient zookeeperClient;

    private ConfigurationCenterClient(ZookeeperClient zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
    }

    public boolean isConnected() {
        return zookeeperClient.isConnected();
    }

    public void close() {
        zookeeperClient.close();
    }

    public void createApplication(String application) throws ConfigurationCenterException {
        AsyncResult<String> result = async(() -> zookeeperClient.createNode(pathOf(application)));

        throwExceptionIfFail(result);
    }

    public void deleteApplication(String application) throws ConfigurationCenterException {
        AsyncResult result = async(() -> zookeeperClient.deleteNode(pathOf(application)));

        throwExceptionIfFail(result);
    }

    public Page<String> getApplications(Pagination pagination) throws ConfigurationCenterException {
        AsyncResult<List<String>> result = async(() -> zookeeperClient.getChildren(pathOfRoot()));

        throwExceptionIfFail(result);

        return pagination(result.getResult(), pagination);
    }

    public void createEnvironment(String application, String environment) throws ConfigurationCenterException {
        AsyncResult<String> result = async(() -> zookeeperClient.createNode(pathOf(application, environment)));

        throwExceptionIfFail(result);
    }

    public void deleteEnvironment(String application, String environment) throws ConfigurationCenterException {
        AsyncResult result = async(() -> zookeeperClient.deleteNode(pathOf(application, environment)));

        throwExceptionIfFail(result);
    }

    public Page<String> getEnvironments(String application, Pagination pagination) throws ConfigurationCenterException {
        AsyncResult<List<String>> result = async(() -> zookeeperClient.getChildren(pathOf(application)));

        throwExceptionIfFail(result);

        return pagination(result.getResult(), pagination);
    }

    public Node getProperty(String application, String environment, String property) throws ConfigurationCenterException {
        AsyncResult<Node> result = async(() -> zookeeperClient.getNode(pathOf(application, environment, property)));

        throwExceptionIfFail(result);

        Node node = result.getResult();
        node.setName(property);
        return node;
    }

    public void createProperty(String application, String environment, String property, String value) throws ConfigurationCenterException {
        AsyncResult<String> result = async(() -> zookeeperClient.createNode(pathOf(application, environment, property), value));

        throwExceptionIfFail(result);
    }

    public Node updateProperty(String application, String environment, String property, String value, int version) throws ConfigurationCenterException {
        AsyncResult<Node> result = async(() -> zookeeperClient.updateNode(pathOf(application, environment, property), value, version));

        throwExceptionIfFail(result);

        Node node = result.getResult();
        node.setName(property);
        return node;
    }

    public void deleteProperty(String application, String environment, String property) throws ConfigurationCenterException {
        AsyncResult result = async(() -> zookeeperClient.deleteNode(pathOf(application, environment, property)));

        throwExceptionIfFail(result);
    }

    public Page<Node> getProperties(String application, String environment, Pagination pagination) throws ConfigurationCenterException {
        AsyncResult<List<String>> result = async(() -> zookeeperClient.getChildren(pathOf(application, environment)));

        throwExceptionIfFail(result);

        Page<String> propertyNames = pagination(result.getResult(), pagination);
        List<Node> nodes = Flowable.fromIterable(propertyNames.getContent())
                .parallel()
                .runOn(io())
                .flatMap(property -> getPropertyWithoutException(application, environment, property))
                .sequential()
                .collect((Callable<List<Node>>) ArrayList::new, List::add)
                .blockingGet();

        return new Page.Builder<Node>()
                .withContent(nodes)
                .withSize(propertyNames.getSize())
                .withNumber(propertyNames.getNumber())
                .withTotalElements(propertyNames.getTotalElements())
                .withTotalPages(propertyNames.getTotalPages())
                .build();
    }

    public void listenProperty(String application, String environment, String property, NodeListener nodeListener) throws ConfigurationCenterException {
        AsyncResult result = async(() -> zookeeperClient.listen(pathOf(application, environment, property), nodeListener));

        throwExceptionIfFail(result);
    }

    private void throwExceptionIfFail(AsyncResult result) throws ConfigurationCenterException {
        if (result.failed()) {
            Exception exception = result.getException();
            LOGGER.error(exception.getMessage(), exception);
            throw new ConfigurationCenterException(exception.getMessage(), exception);
        }
    }

    private Flowable<Node> getPropertyWithoutException(String application, String environment, String property) {
        try {
            return Flowable.just(getProperty(application, environment, property));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Flowable.empty();
        }
    }

    public static class Builder {
        private String connectionString;
        private Integer sessionTimeoutMs;
        private Integer connectionTimeoutMs;
        private Boolean fastFail;

        public Builder connectionString(String connectionString) {
            this.connectionString = connectionString;
            return this;
        }

        public Builder sessionTimeoutMs(Integer sessionTimeoutMs) {
            this.sessionTimeoutMs = sessionTimeoutMs;
            return this;
        }

        public Builder connectionTimeoutMs(Integer connectionTimeoutMs) {
            this.connectionTimeoutMs = connectionTimeoutMs;
            return this;
        }

        public Builder fastFail(boolean fastFail) {
            this.fastFail = fastFail;
            return this;
        }

        public ConfigurationCenterClient build() throws FirstConnectionTimeoutException {
            requireNonNull(connectionString, "connectionString cannot be null");

            ZookeeperClient.Builder builder = new ZookeeperClient.Builder()
                    .connectionString(connectionString);

            setupBuilderIfNotNull(sessionTimeoutMs, () -> builder.sessionTimeoutMs(sessionTimeoutMs));
            setupBuilderIfNotNull(connectionTimeoutMs, () -> builder.connectionTimeoutMs(connectionTimeoutMs));
            setupBuilderIfNotNull(fastFail, () -> builder.fastFail(fastFail));

            ZookeeperClient zookeeperClient = builder.build();
            return new ConfigurationCenterClient(zookeeperClient);
        }

        private void setupBuilderIfNotNull(Object object, Runnable runnable) {
            if (nonNull(object)) {
                runnable.run();
            }
        }
    }
}
