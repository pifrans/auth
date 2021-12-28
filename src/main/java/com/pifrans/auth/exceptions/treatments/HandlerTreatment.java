package com.pifrans.auth.exceptions.treatments;

import com.pifrans.auth.responses.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class HandlerTreatment {
    private static final Logger LOG = LoggerFactory.getLogger(HandlerTreatment.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> runtime(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String field = ((FieldError) error).getField();
            String message = String.format("%s (%s)", error.getDefaultMessage(), ((FieldError) error).getRejectedValue());
            errors.put(field, message);
        });
        LOG.error(errors.toString());
        return ErrorResponse.handle(new String[]{errors.toString()}, request, HttpStatus.NOT_ACCEPTABLE);
    }
}
