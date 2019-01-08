package com.ndrlslz.configuration.center.api.util;

import com.ndrlslz.configuration.center.api.json.common.Data;
import com.ndrlslz.configuration.center.api.json.common.Type;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

public class DataBuilder<T> {
    private Type type;
    private T attributes;
    private Map<String, String> relationships;

    public DataBuilder<T> withType(Type type) {
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
