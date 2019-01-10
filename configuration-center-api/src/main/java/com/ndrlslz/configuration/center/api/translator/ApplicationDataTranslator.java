package com.ndrlslz.configuration.center.api.translator;

import com.ndrlslz.configuration.center.api.json.application.Application;
import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Type;
import com.ndrlslz.configuration.center.api.util.DataBuilder;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class ApplicationDataTranslator extends DataTranslator<String, Application> {
    private static final String RELATIONSHIP_ENVIRONMENTS_KEY = "environments";
    private static final String RELATIONSHIP_ENVIRONMENTS_VALUE = "/applications/%s/environments";

    @Override
    public Data<Application> transform(String app, String... ignored) {
        return new DataBuilder<Application>()
                .withType(Type.APPLICATION)
                .withAttributes(new Application(app))
                .withRelationship(RELATIONSHIP_ENVIRONMENTS_KEY, format(RELATIONSHIP_ENVIRONMENTS_VALUE, app))
                .build();
    }
}
