package com.ndrlslz.configuration.center.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data<T> {
    private Enum type;
    private T attributes;
    private Map<String, String> relationships;

    public Enum getType() {
        return type;
    }

    public void setType(Enum type) {
        this.type = type;
    }

    public T getAttributes() {
        return attributes;
    }

    public void setAttributes(T attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getRelationships() {
        return relationships;
    }

    public void setRelationships(Map<String, String> relationships) {
        this.relationships = relationships;
    }
}
