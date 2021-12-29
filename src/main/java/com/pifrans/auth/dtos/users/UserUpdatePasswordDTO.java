package com.pifrans.auth.dtos.users;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserUpdatePasswordDTO {
    @NotNull
    private Long id;

    @NotNull
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*()--+={}\\[\\]|\\\\:;\"'<>,.?/_]).{8,255}", message = "Senha inválida, a senha deve ter de 8 a 255 caracteres com letras maiúsculas, minusculas, números e caracteres especiais!")
    private String password;
}
