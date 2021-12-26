package com.pifrans.auth.services;

import com.pifrans.auth.models.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService extends GenericService<Profile> {

    @Autowired
    public ProfileService(JpaRepository<Profile, Long> repository) {
        super(repository);
    }
}
