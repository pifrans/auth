package com.pifrans.auth.constants;

import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserGroups {


    public List<String> groupAdmin() {
        return List.of(UserProfiles.ROLE_ADMIN.name());
    }
}
