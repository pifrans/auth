package com.pifrans.auth.dtos.users;

import com.pifrans.auth.models.Profile;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserUpdateProfilesdDTO {
    private Long id;
    private Set<Profile> profiles;
}
