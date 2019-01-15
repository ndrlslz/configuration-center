package com.ndrlslz.configuration.center.api.validation;

import com.ndrlslz.configuration.center.api.exception.InvalidRequestBodyException;
import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Type;
import com.ndrlslz.configuration.center.api.json.property.CreatePropertyRequest;
import com.ndrlslz.configuration.center.api.json.property.Property;
import com.ndrlslz.configuration.center.api.json.property.UpdatePropertyRequest;
import com.ndrlslz.configuration.center.api.util.DataBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PropertyDataValidatorTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private PropertyDataValidator propertyDataValidator = new PropertyDataValidator();

    @Test
    public void shouldValidateCreatePropertyRequest() {
        CreatePropertyRequest createPropertyRequest = new CreatePropertyRequest();

        Property property = new Property();
        property.setName("key");
        property.setValue("value");

        Data<Property> data = new DataBuilder<Property>()
                .withType(Type.PROPERTY)
                .withAttributes(property)
                .build();

        createPropertyRequest.setData(data);

        propertyDataValidator.validate(createPropertyRequest);
    }

    @Test
    public void shouldThrowExceptionWhenCreatePropertyGivenTypeNotMatch() {
        expectedException.expect(InvalidRequestBodyException.class);
        expectedException.expectMessage("type not match, expect property, but receive APPLICATION");

        CreatePropertyRequest createPropertyRequest = new CreatePropertyRequest();

        Property property = new Property();
        property.setName("key");
        property.setValue("value");

        Data<Property> data = new DataBuilder<Property>()
                .withType(Type.APPLICATION)
                .withAttributes(property)
                .build();

        createPropertyRequest.setData(data);

        propertyDataValidator.validate(createPropertyRequest);
    }

    @Test
    public void shouldValidateUpdatePropertyRequest() {
        UpdatePropertyRequest updatePropertyRequest = new UpdatePropertyRequest();

        Property property = new Property();
        property.setName("key");
        property.setValue("value");
        property.setVersion(1);

        Data<Property> data = new DataBuilder<Property>()
                .withType(Type.PROPERTY)
                .withAttributes(property)
                .build();

        updatePropertyRequest.setData(data);

        propertyDataValidator.validateUpdateRequest(updatePropertyRequest);
    }

    @Test
    public void shouldThrowExceptionWhenUpdatePropertyGivenVersionIsNull() {
        expectedException.expect(InvalidRequestBodyException.class);
        expectedException.expectMessage("version cannot be null");

        UpdatePropertyRequest updatePropertyRequest = new UpdatePropertyRequest();

        Property property = new Property();
        property.setName("key");
        property.setValue("value");

        Data<Property> data = new DataBuilder<Property>()
                .withType(Type.PROPERTY)
                .withAttributes(property)
                .build();

        updatePropertyRequest.setData(data);

        propertyDataValidator.validateUpdateRequest(updatePropertyRequest);

    }
}
