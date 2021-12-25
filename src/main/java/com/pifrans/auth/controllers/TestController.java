package com.pifrans.auth.controllers;

import com.pifrans.auth.dtos.users.UserSimpleDTO;
import com.pifrans.auth.mappers.GenericMapper;
import com.pifrans.auth.models.User;
import com.pifrans.auth.repositories.UserRepository;
import com.pifrans.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tests")
public class TestController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GenericMapper<User, UserSimpleDTO> genericMapper;

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<UserSimpleDTO> list = new ArrayList<>();

        for (User user : userRepository.findAll()) {
            list.add(genericMapper.toDto(user, UserSimpleDTO.class));
        }
        return ResponseEntity.ok().body(list);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
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
