package com.ndrlslz.configuration.center.api.translator;

import com.ndrlslz.configuration.center.api.json.common.Data;

import java.util.List;

import static java.util.stream.Collectors.toList;

abstract class DataTranslator<I, O> {
    abstract Data<O> transform(I input);

    public List<Data<O>> translate(List<I> list) {
        return list.stream()
                .map(this::transform)
                .collect(toList());
    }
}
