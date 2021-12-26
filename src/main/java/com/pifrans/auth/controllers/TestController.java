package com.pifrans.auth.controllers;

import com.pifrans.auth.dtos.users.UserSimpleDTO;
import com.pifrans.auth.mappers.GenericMapper;
import com.pifrans.auth.models.User;
import com.pifrans.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tests")
public class TestController extends GenericController<User> {
    private final UserService userService;
    private final GenericMapper<User, UserSimpleDTO> genericMapper;

    @Autowired
    public TestController(UserService userService, GenericMapper<User, UserSimpleDTO> genericMapper) {
        super(userService, User.class);
        this.userService = userService;
        this.genericMapper = genericMapper;
    }


    @Override
    @GetMapping
    public ResponseEntity<?> findAll() {
        List<UserSimpleDTO> list = new ArrayList<>();

        for (User user : userService.findAll()) {
            list.add(genericMapper.toDto(user, UserSimpleDTO.class));
        }
        return ResponseEntity.ok().body(list);
    }
}
