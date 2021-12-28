package com.pifrans.auth.controllers;

import com.pifrans.auth.dtos.users.UserSimpleDTO;
import com.pifrans.auth.dtos.users.UserUpdatePasswordDTO;
import com.pifrans.auth.mappers.GenericMapper;
import com.pifrans.auth.models.User;
import com.pifrans.auth.responses.ErrorResponse;
import com.pifrans.auth.responses.SuccessResponse;
import com.pifrans.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends GenericController<User> {
    private final UserService userService;
    private final GenericMapper<User, UserSimpleDTO> userUserSimpleDTOGenericMapper;
    private final HttpServletRequest request;

    @Autowired
    public UserController(UserService userService, GenericMapper<User, UserSimpleDTO> userUserSimpleDTOGenericMapper, HttpServletRequest request) {
        super(userService, User.class, request);
        this.userService = userService;
        this.userUserSimpleDTOGenericMapper = userUserSimpleDTOGenericMapper;
        this.request = request;
    }


    @Override
    @GetMapping
    public ResponseEntity<?> findAll() {
        try {
            List<UserSimpleDTO> list = new ArrayList<>();
            for (User user : userService.findAll()) {
                list.add(userUserSimpleDTOGenericMapper.toDto(user, UserSimpleDTO.class));
            }
            return SuccessResponse.handle(list, HttpStatus.OK);
        } catch (Exception e) {
            return ErrorResponse.handle(new String[]{e.getMessage()}, request, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UserUpdatePasswordDTO body) {
        try {
            User object = userService.updatePassword(body);
            return SuccessResponse.handle(object, HttpStatus.OK);
        } catch (Exception e) {
            return ErrorResponse.handle(new String[]{e.getMessage()}, request, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
