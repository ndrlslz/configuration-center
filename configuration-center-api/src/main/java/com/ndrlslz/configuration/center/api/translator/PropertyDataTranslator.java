package com.ndrlslz.configuration.center.api.translator;

import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Type;
import com.ndrlslz.configuration.center.api.json.property.Property;
import com.ndrlslz.configuration.center.api.util.DataBuilder;
import com.ndrlslz.configuration.center.core.model.Node;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.lang.String.format;

@Service
public class PropertyDataTranslator extends DataTranslator<Node, Property> {
    private static final String RELATIONSHIP_SELF_KEY = "self";
    private static final String RELATIONSHIP_SELF_VALUE = "/applications/%s/environments/%s/properties/%s";

    @Override
    public Data<Property> transform(Node node, String... relationshipInformation) {
        String application = relationshipInformation[0];
        String environment = relationshipInformation[1];

        Property property = new Property();
        property.setName(node.getName());
        property.setValue(node.getValue());
        property.setVersion(node.getVersion());
        property.setCreateTime(toLocalDateTime(node.getCtime()));
        property.setUpdateTime(toLocalDateTime(node.getMtime()));

        return new DataBuilder<Property>()
                .withType(Type.PROPERTY)
                .withAttributes(property)
                .withRelationship(RELATIONSHIP_SELF_KEY, format(RELATIONSHIP_SELF_VALUE, application, environment, property.getName()))
                .build();
    }

    public Data<Property> wrap(String application, String environment, String name, String value) {
        Property property = new Property();
        property.setName(name);
        property.setValue(value);
        property.setVersion(0);

        return new DataBuilder<Property>()
                .withType(Type.PROPERTY)
                .withAttributes(property)
                .withRelationship(RELATIONSHIP_SELF_KEY, format(RELATIONSHIP_SELF_VALUE, application, environment, property.getName()))
                .build();
    }

    private LocalDateTime toLocalDateTime(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }
}
