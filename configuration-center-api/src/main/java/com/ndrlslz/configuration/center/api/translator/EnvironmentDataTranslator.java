package com.ndrlslz.configuration.center.api.translator;

import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Type;
import com.ndrlslz.configuration.center.api.json.environment.Environment;
import com.ndrlslz.configuration.center.api.util.DataBuilder;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class EnvironmentDataTranslator extends DataTranslator<String, Environment> {
    private static final String RELATIONSHIP_PROPERTIES_KEY = "properties";
    private static final String RELATIONSHIP_PROPERTIES_VALUE = "/applications/%s/environments/%s/properties";

    @Override
    public Data<Environment> transform(String env, String... relationshipInformation) {
        String application = relationshipInformation[0];

        return new DataBuilder<Environment>()
                .withType(Type.ENVIRONMENT)
                .withAttributes(new Environment(env))
                .withRelationship(RELATIONSHIP_PROPERTIES_KEY, format(RELATIONSHIP_PROPERTIES_VALUE, application, env))
                .build();
    }
}
