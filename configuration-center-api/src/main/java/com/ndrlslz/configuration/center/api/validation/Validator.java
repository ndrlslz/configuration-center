package com.ndrlslz.configuration.center.api.validation;

import com.ndrlslz.configuration.center.api.exception.InvalidRequestBodyException;

class Validator {
    static <T> void checkNotNull(T object, String message) {
        if (object == null) {
            throw new InvalidRequestBodyException(message);
        }
    }
}
