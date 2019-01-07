package com.ndrlslz.configuration.center.api.util;

import com.ndrlslz.configuration.center.api.json.Data;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

public class DataBuilder<T> {
    private Enum type;
    private T attributes;
    private Map<String, String> relationships;

    public DataBuilder<T> withType(Enum type) {
        this.type = type;
        return this;
    }

    public DataBuilder<T> withAttributes(T attributes) {
        this.attributes = attributes;
        return this;
    }

    public DataBuilder<T> withRelationship(String key, String value) {
        if (isNull(relationships)) {
            relationships = new HashMap<>();
        }

        relationships.put(key, value);
        return this;
    }

    public Data<T> build() {
        Data<T> data = new Data<>();
        data.setType(type);
        data.setAttributes(attributes);
        data.setRelationships(relationships);

        return data;
    }
}
