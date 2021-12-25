package com.pifrans.auth.dtos.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentialDTO {
    private String email;
    private String password;
}
