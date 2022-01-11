package com.pifrans.auth.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pifrans.auth.dtos.users.UserSimpleDTO;
import com.pifrans.auth.dtos.users.UserUpdatePasswordDTO;
import com.pifrans.auth.dtos.users.UserUpdateProfilesdDTO;
import com.pifrans.auth.dtos.users.UserUpdateSimpleDataDTO;
import com.pifrans.auth.mappers.GenericMapper;
import com.pifrans.auth.models.User;
import com.pifrans.auth.responses.SuccessResponse;
import com.pifrans.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends GenericController<User> {
    private final UserService userService;
    private final GenericMapper<User, UserSimpleDTO> userUserSimpleDTOGenericMapper;

    @Autowired
    public UserController(UserService userService, GenericMapper<User, UserSimpleDTO> userUserSimpleDTOGenericMapper) {
        super(userService, User.class);
        this.userService = userService;
        this.userUserSimpleDTOGenericMapper = userUserSimpleDTOGenericMapper;
    }

    @Override
    @GetMapping
    public ResponseEntity<?> findAll() {
        List<UserSimpleDTO> list = new ArrayList<>();
        for (User user : userService.findAll()) {
            list.add(userUserSimpleDTOGenericMapper.toDto(user, UserSimpleDTO.class));
        }
        return SuccessResponse.handle(list, HttpStatus.OK);
    }

    @PutMapping("/updateSimpleData")
    public ResponseEntity<?> updateSimpleData(@Valid @RequestBody UserUpdateSimpleDataDTO body) throws JsonProcessingException {
        User object = userService.updateSimpleData(body);
        return SuccessResponse.handle(object, HttpStatus.OK);
    }

    @PutMapping("/updateActive/{id}/{isActive}")
    public ResponseEntity<?> updateActive(@PathVariable Long id, @PathVariable Boolean isActive) {
        User object = userService.updateActive(id, isActive);
        return SuccessResponse.handle(object, HttpStatus.OK);
    }

    @PutMapping("/updateProfiles")
    public ResponseEntity<?> updateProfiles(@Valid @RequestBody UserUpdateProfilesdDTO body) {
        User object = userService.updateProfiles(body);
        return SuccessResponse.handle(object, HttpStatus.OK);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UserUpdatePasswordDTO body) {
        User object = userService.updatePassword(body);
        return SuccessResponse.handle(object, HttpStatus.OK);
    }
}
