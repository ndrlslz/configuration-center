package com.ndrlslz.configuration.center.api.translator;

import com.ndrlslz.configuration.center.api.json.common.Data;

import java.util.List;

import static java.util.stream.Collectors.toList;

abstract class DataTranslator<I, O> {
    abstract Data<O> transform(I input, String... relationshipInformation);

    public List<Data<O>> translate(List<I> list, String... relationshipInformation) {
        return list.stream()
                .map(input -> transform(input, relationshipInformation))
                .collect(toList());
    }
}
