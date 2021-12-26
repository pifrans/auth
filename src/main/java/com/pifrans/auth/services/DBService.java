package com.pifrans.auth.services;

import com.pifrans.auth.constants.UserProfiles;
import com.pifrans.auth.models.Profile;
import com.pifrans.auth.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class DBService {
    private final UserService userService;
    private final ProfileService profileService;

    @Autowired
    public DBService(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    public void init() {
        Profile p1 = Profile.builder().permission(UserProfiles.ROLE_ADMIN).build();
        Profile p2 = Profile.builder().permission(UserProfiles.ROLE_USER).build();

        User u1 = User.builder().name("admin").email("admin@gmail.com").password("admin").isActive(true).profiles(Set.copyOf(List.of(p1, p2))).build();
        User u2 = User.builder().name("user").email("user@gmail.com").password("user").isActive(true).build();

        try {
            profileService.saveAll(List.of(p1, p2));
            userService.saveAll(Arrays.asList(u1, u2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
