package com.pifrans.auth.controllers;

import com.pifrans.auth.models.Profile;
import com.pifrans.auth.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profiles")
public class ProfileController extends GenericController<Profile> {

    @Autowired
    public ProfileController(ProfileService profileService) {
        super(profileService, Profile.class);
    }
}
