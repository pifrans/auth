package com.pifrans.auth.exceptions.treatments;

import com.pifrans.auth.exceptions.errors.PermissionException;
import com.pifrans.auth.responses.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

@ControllerAdvice
public class HandlerTreatment {
    private static final Logger LOG = LoggerFactory.getLogger(HandlerTreatment.class);
    private final HttpServletRequest request;

    @Autowired
    public HandlerTreatment(HttpServletRequest request) {
        this.request = request;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handler(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String field = ((FieldError) error).getField();
            String message = String.format("%s (%s)", error.getDefaultMessage(), ((FieldError) error).getRejectedValue());
            errors.put(field, message);
        });
        LOG.error(exception.getStackTrace()[36].toString() + " --> " + errors);
        return ErrorResponse.handle(new String[]{errors.toString()}, exception.getStackTrace()[36], request, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handler(MethodArgumentTypeMismatchException exception) {
        LOG.error(exception.getStackTrace()[36].toString() + " --> " + exception.getCause().getMessage());
        return ErrorResponse.handle(new String[]{exception.getCause().getMessage()}, exception.getStackTrace()[36], request, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<?> handler(PermissionException exception) {
        LOG.error(exception.getStackTrace()[0].toString() + " --> " + exception.getMessage());
        return ErrorResponse.handle(new String[]{exception.getMessage()}, exception.getStackTrace()[0], request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handler(NoSuchElementException exception) {
        LOG.error(exception.getStackTrace()[0].toString() + " --> " + exception.getMessage());
        return ErrorResponse.handle(new String[]{exception.getMessage()}, exception.getStackTrace()[0], request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handler(DataIntegrityViolationException exception) {
        LOG.error(exception.getStackTrace()[13].toString() + " --> " + Objects.requireNonNull(exception.getRootCause()).getMessage());
        return ErrorResponse.handle(new String[]{exception.getRootCause().getMessage()}, exception.getStackTrace()[13], request, HttpStatus.CONFLICT);
    }
}
