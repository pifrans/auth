package com.pifrans.auth.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentialDTO {
    private String email;
    private String password;
}
