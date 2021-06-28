package com.pifrans.auth.controllers;

import com.pifrans.auth.models.User;
import com.pifrans.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class Teste {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/users")
    public ResponseEntity<?> findAll() {
        List<User> list = userRepository.findAll();
        return ResponseEntity.ok().body(list);
    }
}
