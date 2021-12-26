package com.pifrans.auth.controllers;

import com.pifrans.auth.constants.ReflectMethods;
import com.pifrans.auth.services.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public abstract class GenericController<T> {
    private final GenericService<T> service;
    private final Class<T> tClass;


    @Autowired
    public GenericController(GenericService<T> service, Class<T> tClass) {
        this.service = service;
        this.tClass = tClass;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            T object = service.findById(tClass, id);
            return ResponseEntity.ok().body(object);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<T> list = service.findAll();
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody T body) {
        try {
            T object = service.save(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(object);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody T body) {
        try {
            Method method = body.getClass().getMethod(ReflectMethods.GET_ID.getDescription());
            Long id = (Long) method.invoke(body);
            T object = service.update(body, id);
            return ResponseEntity.status(HttpStatus.OK).body(object);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
