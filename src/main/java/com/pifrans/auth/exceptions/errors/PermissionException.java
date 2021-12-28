package com.pifrans.auth.exceptions.errors;

public class PermissionException extends RuntimeException {

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(String message, Exception e) {
        super(message, e);
    }
}
