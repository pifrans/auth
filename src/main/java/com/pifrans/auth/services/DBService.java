package com.pifrans.auth.services;

import com.pifrans.auth.constants.UserProfiles;
import com.pifrans.auth.models.Profile;
import com.pifrans.auth.models.User;
import com.pifrans.auth.repositories.ProfileRepository;
import com.pifrans.auth.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class DBService {
    private static final Logger LOG = Logger.getLogger(DBService.class.getName());
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public DBService(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }


    @Transactional
    public void init() {
        Profile p1 = Profile.builder().permission(UserProfiles.ROLE_ADMIN.name()).build();
        Profile p2 = Profile.builder().permission(UserProfiles.ROLE_USER.name()).build();

        User u1 = new User();
        u1.setName("admin");
        u1.setEmail("admin@gmail.com");
        u1.setPassword("@Lu12345");
        u1.setActive(true);
        u1.setProfiles(Set.copyOf(List.of(p1, p2)));

        User u2 = new User();
        u2.setName("user");
        u2.setEmail("user@gmail.com");
        u2.setPassword("@Lu12345");
        u2.setActive(true);

        try {
            profileRepository.saveAll(List.of(p1, p2));
            userRepository.saveAll(Arrays.asList(u1, u2));
        } catch (Exception e) {
            LOG.severe(e.getMessage());
        }
    }
}
