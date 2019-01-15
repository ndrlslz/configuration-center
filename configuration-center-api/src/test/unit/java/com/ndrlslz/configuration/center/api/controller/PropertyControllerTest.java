package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.api.exception.InvalidRequestBodyException;
import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Type;
import com.ndrlslz.configuration.center.api.json.property.*;
import com.ndrlslz.configuration.center.api.service.PropertyService;
import com.ndrlslz.configuration.center.api.validation.PropertyDataValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PropertyControllerTest {
    private final static PageRequest PAGE = new PageRequest(1, 10);

    @Mock
    private PropertyService propertyService;

    private PropertyDataValidator validator = new PropertyDataValidator();

    private PropertyController propertyController;

    @Before
    public void setUp() {
        initMocks(this);
        propertyController = new PropertyController(propertyService, validator);
    }

    @Test
    public void shouldGetProperties() {
        GetPropertiesResponse expected = new GetPropertiesResponse();

        when(propertyService.getProperties("customer-api", "dev", PAGE)).thenReturn(expected);

        GetPropertiesResponse response = propertyController.getProperties("customer-api", "dev", PAGE);

        assertThat(response, is(expected));
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

        CreatePropertyResponse expected = new CreatePropertyResponse();

        when(propertyService.createProperty("customer-api", "dev", createPropertyRequest)).thenReturn(expected);

        CreatePropertyResponse response = propertyController.createProperty("customer-api", "dev", createPropertyRequest);

        assertThat(response, is(expected));
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowExceptionWhenCreatePropertyGivenTypeNotMatch() {
        CreatePropertyRequest createPropertyRequest = new CreatePropertyRequest();
        Data<Property> data = new Data<>();
        data.setType(Type.ENVIRONMENT);
        Property property = new Property();
        property.setName("key");
        property.setValue("value");
        data.setAttributes(property);
        createPropertyRequest.setData(data);

        propertyController.createProperty("customer-api", "dev", createPropertyRequest);
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

        UpdatePropertyResponse expected = new UpdatePropertyResponse();
        when(propertyService.updateProperty("customer-api", "dev", "key", updatePropertyRequest)).thenReturn(expected);

        UpdatePropertyResponse response = propertyController.updateProperty("customer-api", "dev", "key", updatePropertyRequest);

        assertThat(response, is(expected));
    }

    @Test(expected = InvalidRequestBodyException.class)
    public void shouldThrowExceptionWhenUpdatePropertyGivenVersionIsEmpty() {
        UpdatePropertyRequest updatePropertyRequest = new UpdatePropertyRequest();
        Data<Property> data = new Data<>();
        data.setType(Type.PROPERTY);
        Property property = new Property();
        property.setValue("value");
        data.setAttributes(property);
        updatePropertyRequest.setData(data);

        propertyController.updateProperty("customer-api", "dev", "key", updatePropertyRequest);
    }

    @Test
    public void shouldDeleteProperty() {
        Mockito.doNothing().when(propertyService).deleteProperty(any(), any(), any());

        propertyController.deleteProperty("customer-api", "dev", "key");
    }
}
