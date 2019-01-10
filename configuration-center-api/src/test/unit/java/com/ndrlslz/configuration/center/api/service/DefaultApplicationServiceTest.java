package com.ndrlslz.configuration.center.api.service;

import com.ndrlslz.configuration.center.api.dao.ApplicationDao;
import com.ndrlslz.configuration.center.api.json.application.Application;
import com.ndrlslz.configuration.center.api.json.application.CreateApplicationRequest;
import com.ndrlslz.configuration.center.api.json.application.CreateApplicationResponse;
import com.ndrlslz.configuration.center.api.json.application.GetApplicationsResponse;
import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Type;
import com.ndrlslz.configuration.center.api.translator.ApplicationDataTranslator;
import com.ndrlslz.configuration.center.api.util.DataBuilder;
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

public class DefaultApplicationServiceTest {
    private final static PageRequest PAGE = new PageRequest(1, 10);

    @Mock
    private ApplicationDao dao;

    private ApplicationDataTranslator applicationDataTranslator = new ApplicationDataTranslator();

    private DefaultApplicationService service;

    @Before
    public void setUp() {
        initMocks(this);
        service = new DefaultApplicationService(dao, applicationDataTranslator);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @Test
    public void shouldGetApplications() {
        Page<String> expect = new Page.Builder<String>()
                .withTotalPages(1)
                .withTotalElements(3)
                .withNumber(0)
                .withSize(10)
                .withContent(Arrays.asList("customer-api", "order-api", "product-api"))
                .build();

        when(dao.getApplications(any())).thenReturn(expect);

        GetApplicationsResponse response = service.getApplications(PAGE);

        List<String> applications = response.getData().stream().map(applicationData -> applicationData.getAttributes().getName()).collect(toList());
        assertThat(response.getData().size(), is(3));
        assertThat(applications, hasItems("customer-api", "order-api", "product-api"));
    }

    @Test
    public void shouldCreateApplication() {
        CreateApplicationRequest request = new CreateApplicationRequest();
        Data<Application> data = new DataBuilder<Application>()
                .withType(Type.APPLICATION)
                .withAttributes(new Application("customer-api"))
                .build();
        request.setData(data);

        Mockito.doNothing().when(dao).createApplication("customer-api");

        CreateApplicationResponse response = service.createApplication(request);

        assertThat(response.getData().getType(), is(Type.APPLICATION));
        assertThat(response.getData().getAttributes().getName(), is("customer-api"));
        assertThat(response.getData().getRelationships().get("environments"), is("/applications/customer-api/environments"));
    }

    @Test
    public void shouldDeleteApplication() {
        Mockito.doNothing().when(dao).deleteApplication("customer-api");

        service.deleteApplication("customer-api");
    }
}