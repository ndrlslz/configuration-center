package com.ndrlslz.configuration.center.api.service;

import com.ndrlslz.configuration.center.api.dao.PropertyDao;
import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Type;
import com.ndrlslz.configuration.center.api.json.property.*;
import com.ndrlslz.configuration.center.api.translator.PropertyDataTranslator;
import com.ndrlslz.configuration.center.core.model.Node;
import com.ndrlslz.configuration.center.core.model.Page;
import org.apache.zookeeper.data.Stat;
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

public class DefaultPropertyServiceTest {
    private final static PageRequest PAGE = new PageRequest(1, 10);

    @Mock
    private PropertyDao propertyDao;

    private PropertyDataTranslator propertyDataTranslator = new PropertyDataTranslator();

    private DefaultPropertyService service;

    @Before
    public void setUp() {
        initMocks(this);
        service = new DefaultPropertyService(propertyDao, propertyDataTranslator);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @Test
    public void shouldGetProperties() {
        Page<Node> expected = new Page.Builder<Node>()
                .withTotalPages(1)
                .withTotalElements(3)
                .withNumber(0)
                .withSize(10)
                .withContent(Arrays.asList(nodeOf("key1", "value1"), nodeOf("key2", "value2"), nodeOf("key3", "value3")))
                .build();

        when(propertyDao.getProperties(any(), any(), any())).thenReturn(expected);

        GetPropertiesResponse response = service.getProperties("customer-api", "dev", PAGE);
        List<String> propertiesName = response.getData().stream().map(propertyData -> propertyData.getAttributes().getName()).collect(toList());
        List<String> propertiesValue = response.getData().stream().map(propertyData -> propertyData.getAttributes().getValue()).collect(toList());

        assertThat(response.getData().size(), is(3));
        assertThat(propertiesName, hasItems("key1", "key2", "key3"));
        assertThat(propertiesValue, hasItems("value1", "value2", "value3"));
    }

    @Test
    public void shouldCreateProperty() {
        CreatePropertyRequest createPropertyRequest = new CreatePropertyRequest();
        Data<Property> data = new Data<>();
        data.setType(Type.PROPERTY);
        Property property = new Property();
        property.setName("key");
        property.setValue("value");
        data.setAttributes(property);
        createPropertyRequest.setData(data);

        Mockito.doNothing().when(propertyDao).createProperty("customer-api", "dev", "key", "value");

        CreatePropertyResponse response = service.createProperty("customer-api", "dev", createPropertyRequest);

        assertThat(response.getData().getType(), is(Type.PROPERTY));
        assertThat(response.getData().getAttributes().getName(), is("key"));
        assertThat(response.getData().getAttributes().getValue(), is("value"));
        assertThat(response.getData().getAttributes().getVersion(), is(0));
    }

    @Test
    public void shouldUpdateProperty() {
        UpdatePropertyRequest updatePropertyRequest = new UpdatePropertyRequest();
        Data<Property> data = new Data<>();
        data.setType(Type.PROPERTY);
        Property property = new Property();
        property.setValue("value");
        property.setVersion(1);
        data.setAttributes(property);
        updatePropertyRequest.setData(data);

        when(propertyDao.updateProperty("customer-api", "dev", "key", "value", 1)).thenReturn(nodeOf("key", "value"));

        UpdatePropertyResponse response = service.updateProperty("customer-api", "dev", "key", updatePropertyRequest);

        assertThat(response.getData().getType(), is(Type.PROPERTY));
        assertThat(response.getData().getAttributes().getName(), is("key"));
        assertThat(response.getData().getAttributes().getValue(), is("value"));
    }

    private Node nodeOf(String key, String value) {
        Node node = new Node.Builder()
                .withStat(new Stat())
                .withValue(value)
                .build();

        node.setName(key);
        return node;
    }
}
