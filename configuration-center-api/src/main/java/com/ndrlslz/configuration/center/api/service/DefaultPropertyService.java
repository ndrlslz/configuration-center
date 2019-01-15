package com.ndrlslz.configuration.center.api.service;

import com.ndrlslz.configuration.center.api.dao.PropertyDao;
import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.property.*;
import com.ndrlslz.configuration.center.api.translator.PropertyDataTranslator;
import com.ndrlslz.configuration.center.api.util.LinksBuilder;
import com.ndrlslz.configuration.center.api.util.MetadataBuilder;
import com.ndrlslz.configuration.center.core.model.Node;
import com.ndrlslz.configuration.center.core.model.Page;
import com.ndrlslz.configuration.center.core.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DefaultPropertyService implements PropertyService {
    private PropertyDao propertyDao;

    private PropertyDataTranslator propertyDataTranslator;

    @Autowired
    public DefaultPropertyService(PropertyDao propertyDao, PropertyDataTranslator propertyDataTranslator) {
        this.propertyDao = propertyDao;
        this.propertyDataTranslator = propertyDataTranslator;
    }

    @Override
    public GetPropertiesResponse getProperties(String application, String environment, Pageable pageable) {
        Pagination pagination = new Pagination.Builder()
                .withNumber(pageable.getPageNumber())
                .withSize(pageable.getPageSize())
                .build();

        Page<Node> properties = propertyDao.getProperties(application, environment, pagination);

        GetPropertiesResponse getPropertiesResponse = new GetPropertiesResponse();
        getPropertiesResponse.setData(propertyDataTranslator.translate(properties.getContent(), application, environment));
        getPropertiesResponse.setLinks(new LinksBuilder().withPage(properties).build());
        getPropertiesResponse.setMetadata(new MetadataBuilder().withPage(properties).build());

        return getPropertiesResponse;
    }

    @Override
    public CreatePropertyResponse createProperty(String application, String environment, CreatePropertyRequest request) {
        String name = request.getData().getAttributes().getName();
        String value = request.getData().getAttributes().getValue();

        propertyDao.createProperty(application, environment, name, value);

        Data<Property> data = propertyDataTranslator.wrap(application, environment, name, value);
        CreatePropertyResponse createPropertyResponse = new CreatePropertyResponse();
        createPropertyResponse.setData(data);

        return createPropertyResponse;
    }

    @Override
    public UpdatePropertyResponse updateProperty(String application, String environment, String name, UpdatePropertyRequest request) {
        Property attributes = request.getData().getAttributes();
        String value = attributes.getValue();
        int version = attributes.getVersion();

        Node node = propertyDao.updateProperty(application, environment, name, value, version);

        UpdatePropertyResponse updatePropertyResponse = new UpdatePropertyResponse();
        Data<Property> data = propertyDataTranslator.transform(node, application, environment);
        updatePropertyResponse.setData(data);

        return updatePropertyResponse;
    }
}
