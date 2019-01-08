package com.ndrlslz.configuration.center.api.json.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum Type {
    APPLICATION("application"),

    ENVIRONMENT("environment"),

    PROPERTY("property");

    private static Map<String, Type> NAME_MAP = Stream
            .of(Type.values())
            .collect(toMap(type -> type.name, Function.identity()));

    private final String name;

    Type(String name) {
        this.name = name;
    }

    @JsonCreator
    public static Type fromName(String name) {
        return Optional
                .ofNullable(NAME_MAP.get(name))
                .orElse(null);
    }

    @JsonValue
    public String toName() {
        return this.name;
    }
}
