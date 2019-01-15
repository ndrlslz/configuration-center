package com.ndrlslz.configuration.center.api.service;

import com.ndrlslz.configuration.center.api.dao.EnvironmentDao;
import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Type;
import com.ndrlslz.configuration.center.api.json.environment.CreateEnvironmentRequest;
import com.ndrlslz.configuration.center.api.json.environment.CreateEnvironmentResponse;
import com.ndrlslz.configuration.center.api.json.environment.Environment;
import com.ndrlslz.configuration.center.api.json.environment.GetEnvironmentsResponse;
import com.ndrlslz.configuration.center.api.translator.EnvironmentDataTranslator;
import com.ndrlslz.configuration.center.core.model.Page;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultEnvironmentServiceTest {
    private final static PageRequest PAGE = new PageRequest(1, 10);

    @Mock
    private EnvironmentDao dao;

    private EnvironmentDataTranslator environmentDataTranslator = new EnvironmentDataTranslator();

    private DefaultEnvironmentService service;

    @Before
    public void setUp() {
        initMocks(this);
        service = new DefaultEnvironmentService(dao, environmentDataTranslator);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @Test
    public void shouldGetEnvironments() {
        Page<String> expect = new Page.Builder<String>()
                .withTotalPages(1)
                .withTotalElements(3)
                .withNumber(0)
                .withSize(10)
                .withContent(Arrays.asList("dev", "uat", "prod"))
                .build();

        when(dao.getEnvironments(any(), any())).thenReturn(expect);

        GetEnvironmentsResponse response = service.getEnvironments("custoemr-api", PAGE);
        List<String> environments = response.getData().stream().map(environmentData -> environmentData.getAttributes().getName()).collect(toList());

        assertThat(response.getData().size(), is(3));
        assertThat(environments, hasItems("dev", "uat", "prod"));
    }

    @Test
    public void shouldCreateEnvironment() {
        CreateEnvironmentRequest createEnvironmentRequest = new CreateEnvironmentRequest();
        Data<Environment> data = new Data<>();
        data.setType(Type.ENVIRONMENT);
        data.setAttributes(new Environment("dev"));
        createEnvironmentRequest.setData(data);

        Mockito.doNothing().when(dao).createEnvironment("customer-api", "dev");

        CreateEnvironmentResponse response = service.createEnvironment("customer-api", createEnvironmentRequest);

        assertThat(response.getData().getType(), is(Type.ENVIRONMENT));
        assertThat(response.getData().getAttributes().getName(), is("dev"));
        assertThat(response.getData().getRelationships().get("properties"), is("/applications/customer-api/environments/dev/properties"));
    }

    @Test
    public void shouldDeleteEnvironment() {
        Mockito.doNothing().when(dao).deleteEnvironment("customer-api", "dev");

        service.deleteEnvironment("customer-api", "dev");
    }
}