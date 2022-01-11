package com.pifrans.auth.controllers;

import com.pifrans.auth.constants.ReflectMethods;
import com.pifrans.auth.responses.SuccessResponse;
import com.pifrans.auth.services.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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
        T object = service.findById(tClass, id);
        return SuccessResponse.handle(object, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<T> list = service.findAll();
        return SuccessResponse.handle(list, HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<T>> findByPage(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "24") Integer linesPerPage, @RequestParam(defaultValue = "id") String orderBy, @RequestParam(defaultValue = "ASC") String direction) {
        Page<T> list = service.findByPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody T body) {
        T object = service.save(body);
        return SuccessResponse.handle(object, HttpStatus.CREATED);
    }

    @PostMapping("/saveAll")
    public ResponseEntity<?> saveAll(@Valid @RequestBody List<T> body) {
        List<T> list = service.saveAll(body);
        return SuccessResponse.handle(list, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody T body) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = body.getClass().getMethod(ReflectMethods.GET_ID.getDescription());
        Long id = (Long) method.invoke(body);
        T object = service.update(body, id);
        return SuccessResponse.handle(object, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        T object = service.deleteById(tClass, id);
        return SuccessResponse.handle(object, HttpStatus.OK);
    }
}
