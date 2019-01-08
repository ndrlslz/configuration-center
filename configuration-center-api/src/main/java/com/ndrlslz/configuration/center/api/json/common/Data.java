package com.ndrlslz.configuration.center.api.json.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data<T> {
    @JsonProperty("type")
    @ApiModelProperty(value = "Data Type Information", required = true, position = 1)
    private Type type;

    @JsonProperty("attributes")
    @ApiModelProperty(value = "Data Attributes Information", required = true, position = 2)
    private T attributes;

    @JsonProperty("relationships")
    @ApiModelProperty(value = "Data Relationships Information", position = 3)
    private Map<String, String> relationships;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
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

    @Override
    public String toString() {
        return "Data{" +
                "type=" + type +
                ", attributes=" + attributes +
                ", relationships=" + relationships +
                '}';
    }
}
