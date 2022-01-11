package com.pifrans.auth.exceptions.errors;

public class PropertyValueException extends RuntimeException {

    public PropertyValueException(String message) {
        super(message);
    }

    public PropertyValueException(String message, Exception e) {
        super(message, e);
    }
}
