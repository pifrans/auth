package com.pifrans.auth.services;

import com.pifrans.auth.models.Profile;
import com.pifrans.auth.models.User;
import com.pifrans.auth.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class DBService {
    @Autowired
    private UserService userService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public void init() {
        Profile p1 = Profile.builder().permission("ROLE_ADMIN").build();
        Profile p2 = Profile.builder().permission("ROLE_USER").build();

        User u1 = User.builder()
                .name("admin")
                .email("admin@gmail.com")
                .password(encoder.encode("admin"))
                .isActive(true)
                .lastAccess(new Date(System.currentTimeMillis()))
                .profiles(Set.copyOf(List.of(p1, p2)))
                .build();

        User u2 = User.builder()
                .name("user")
                .email("user@gmail.com")
                .password(encoder.encode("user"))
                .isActive(true)
                .lastAccess(new Date(System.currentTimeMillis()))
                .profiles(Set.copyOf(List.of(p2)))
                .build();

        try {
            profileRepository.saveAll(List.of(p1, p2));
            userService.saveAll(Arrays.asList(u1, u2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
