package com.pifrans.auth.dtos.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserSimpleDTO {
    private Long id;
    private String name;
    private String email;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date lastAccess;

    private boolean isActive;
    private Integer profiles;
}
