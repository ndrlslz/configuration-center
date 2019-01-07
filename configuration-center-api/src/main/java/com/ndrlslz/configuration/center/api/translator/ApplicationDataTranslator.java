package com.ndrlslz.configuration.center.api.translator;

import com.ndrlslz.configuration.center.api.json.Data;
import com.ndrlslz.configuration.center.api.model.Application;
import com.ndrlslz.configuration.center.api.model.Type;
import com.ndrlslz.configuration.center.api.util.DataBuilder;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class ApplicationDataTranslator extends DataTranslator<String, Application> {

    private static final String ENVIRONMENTS_RELATIONSHIP_KEY = "environments";
    private static final String ENVIRONMENTS_RELATIONSHIP_VALUE = "/applications/%s/environments";

    @Override
    Data<Application> transform(String app) {
        return new DataBuilder<Application>()
                .withType(Type.APPLICATION)
                .withAttributes(new Application(app))
                .withRelationship(ENVIRONMENTS_RELATIONSHIP_KEY, format(ENVIRONMENTS_RELATIONSHIP_VALUE, app))
                .build();
    }
}
