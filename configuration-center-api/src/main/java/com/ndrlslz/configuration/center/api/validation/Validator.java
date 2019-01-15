package com.ndrlslz.configuration.center.api.validation;

import com.ndrlslz.configuration.center.api.exception.InvalidRequestBodyException;

interface Validator<T> {
    void validate(T request);

    default void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new InvalidRequestBodyException(message);
        }
    }

    default void checkState(boolean expression, String message) {
        if (!expression) {
            throw new InvalidRequestBodyException(message);
        }
    }
}
