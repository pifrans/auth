package com.pifrans.auth.controllers;

import com.pifrans.auth.models.User;
import com.pifrans.auth.repositories.UserRepository;
import com.pifrans.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testes")
public class Teste {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<User> list = userRepository.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody User user) {
        try {
            User object = userService.save(user);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(object);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody User user) {
        try {
            User object = userService.update(user);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(object);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
