package com.example.onekids_project.security.payload;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ChangePasswordRequest {

    @Length(min = 6)
    private String oldPassword;

    @Length(min = 6)
    private String password;

    @Length(min = 6)
    private String confirmPassword;

}
