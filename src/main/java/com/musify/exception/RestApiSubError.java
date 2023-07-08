package com.musify.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

abstract class RestApiSubError {
}

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
class RestApiValidationError extends RestApiSubError {

    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    RestApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}
