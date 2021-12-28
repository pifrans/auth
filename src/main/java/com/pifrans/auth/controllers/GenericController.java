package com.pifrans.auth.controllers;

import com.pifrans.auth.constants.ReflectMethods;
import com.pifrans.auth.responses.ErrorResponse;
import com.pifrans.auth.responses.SuccessResponse;
import com.pifrans.auth.services.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
public abstract class GenericController<T> {
    private final GenericService<T> service;
    private final Class<T> tClass;
    private final HttpServletRequest request;


    @Autowired
    public GenericController(GenericService<T> service, Class<T> tClass, HttpServletRequest request) {
        this.service = service;
        this.tClass = tClass;
        this.request = request;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            T object = service.findById(tClass, id);
            return SuccessResponse.handle(object, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ErrorResponse.handle(new String[]{e.getMessage()}, request, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ErrorResponse.handle(new String[]{e.getMessage()}, request, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<T> list = service.findAll();
            return SuccessResponse.handle(list, HttpStatus.OK);
        } catch (Exception e) {
            return ErrorResponse.handle(new String[]{e.getMessage()}, request, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody T body) {
        try {
            T object = service.save(body);
            return SuccessResponse.handle(object, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return ErrorResponse.handle(new String[]{Objects.requireNonNull(e.getRootCause()).getMessage()}, request, HttpStatus.CONFLICT);
        } catch (Exception e) {
            return ErrorResponse.handle(new String[]{e.getMessage()}, request, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/saveAll")
    public ResponseEntity<?> saveAll(@RequestBody List<T> body) {
        try {
            List<T> list = service.saveAll(body);
            return SuccessResponse.handle(list, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return ErrorResponse.handle(new String[]{Objects.requireNonNull(e.getRootCause()).getMessage()}, request, HttpStatus.CONFLICT);
        } catch (Exception e) {
            return ErrorResponse.handle(new String[]{e.getMessage()}, request, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody T body) {
        try {
            Method method = body.getClass().getMethod(ReflectMethods.GET_ID.getDescription());
            Long id = (Long) method.invoke(body);
            T object = service.update(body, id);
            return SuccessResponse.handle(object, HttpStatus.OK);
        } catch (Exception e) {
            return ErrorResponse.handle(new String[]{e.getMessage()}, request, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
