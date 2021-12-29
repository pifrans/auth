package com.pifrans.auth.dtos.users;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UserUpdateSimpleDataDTO extends UserUpdatePasswordDTO {
    private String name;

    @Email(message = "E-mail inválido!")
    private String email;
}
