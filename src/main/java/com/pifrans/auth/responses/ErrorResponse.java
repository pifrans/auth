package com.pifrans.auth.responses;

import com.pifrans.auth.exceptions.treatments.StandardTreatment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ErrorResponse {

    private static final String KEY_ERRORS = "errors";
    private static final String KEY_GLOBAL = "global";

    private ErrorResponse() {
    }

    public static ResponseEntity<?> handle(Errors e, HttpStatus status) {
        final List<StandardTreatment> errors = new ArrayList<>();

        for (final FieldError error : e.getFieldErrors()) {
            errors.add(new StandardTreatment(error.getField(), "field", error.getDefaultMessage()));
        }

        for (final ObjectError error : e.getGlobalErrors()) {
            errors.add(new StandardTreatment(error.getObjectName(), KEY_GLOBAL, error.getDefaultMessage()));
        }

        HashMap<String, List<StandardTreatment>> result = new HashMap<>();
        result.put(KEY_ERRORS, errors);

        return new ResponseEntity<>(result, status);
    }

    public static ResponseEntity<?> handle(String[] messages, Class<?> klass, HttpStatus status) {
        final List<StandardTreatment> errors = new ArrayList<>();

        for (final String message : messages) {
            errors.add(new StandardTreatment(klass.getSimpleName(), KEY_GLOBAL, message));
        }

        HashMap<String, List<StandardTreatment>> result = new HashMap<>();
        result.put(KEY_ERRORS, errors);

        return new ResponseEntity<>(result, status);
    }

    public static ResponseEntity<?> handle(String message, Class<?> klass, HttpStatus status) {
        final List<StandardTreatment> errors = new ArrayList<>();

        errors.add(new StandardTreatment(klass.getSimpleName(), KEY_GLOBAL, message));

        HashMap<String, List<StandardTreatment>> result = new HashMap<>();
        result.put(KEY_ERRORS, errors);

        return new ResponseEntity<>(result, status);
    }

    public static ResponseEntity<?> handle(String[] messages, HttpServletRequest request, HttpStatus status) {
        final List<StandardTreatment> errors = new ArrayList<>();

        for (final String message : messages) {
            errors.add(new StandardTreatment(Thread.currentThread().getStackTrace()[2].toString(), KEY_GLOBAL, message, request.getMethod(), request.getRequestURI()));
        }

        HashMap<String, List<StandardTreatment>> result = new HashMap<>();
        result.put(KEY_ERRORS, errors);

        return new ResponseEntity<>(result, status);
    }
}
