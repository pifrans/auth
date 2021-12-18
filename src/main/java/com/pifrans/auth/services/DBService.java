package com.pifrans.auth.services;

import com.pifrans.auth.models.Record;
import com.pifrans.auth.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

@Service
public class DBService {
    @Autowired
    private UserService userService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public void init() {
        User u1 = User.builder().name("admin").email("admin@gmail.com").password(encoder.encode("admin")).build();
        Record r1 = Record.builder().creationDate(new Date(System.currentTimeMillis())).tableName("users").build();

        User u2 = User.builder().name("user").email("user@gmail.com").password(encoder.encode("user")).build();
        Record r2 = Record.builder().creationDate(new Date(System.currentTimeMillis())).tableName("users").build();

        userService.saveAll(Arrays.asList(u1, u2));
        recordService.saveAll(Arrays.asList(r1, r2));

    }
}
