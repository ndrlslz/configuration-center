package com.ndrlslz.configuration.center.api.translator;

import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.application.Application;
import com.ndrlslz.configuration.center.api.json.common.Type;
import com.ndrlslz.configuration.center.api.util.DataBuilder;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class ApplicationDataTranslator extends DataTranslator<String, Application> {

    private static final String ENVIRONMENTS_RELATIONSHIP_KEY = "environments";
    private static final String ENVIRONMENTS_RELATIONSHIP_VALUE = "/applications/%s/environments";

    @Override
    public Data<Application> transform(String app) {
        return new DataBuilder<Application>()
                .withType(Type.APPLICATION)
                .withAttributes(new Application(app))
                .withRelationship(ENVIRONMENTS_RELATIONSHIP_KEY, format(ENVIRONMENTS_RELATIONSHIP_VALUE, app))
                .build();
    }
}
