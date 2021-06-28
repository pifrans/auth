package com.pifrans.auth.services;

import com.pifrans.auth.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DBService {
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public void init() {
        User u1 = User.builder().name("admin").password(encoder.encode("admin")).build();
        User u2 = User.builder().name("user").password(encoder.encode("user")).build();
        userService.saveAll(Arrays.asList(u1, u2));
    }
}
