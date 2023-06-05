package com.example.onekids_project.security.payload;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class SignUpRequest {
    @NotBlank
    @Size(max = 255)
    private String fullName;

    @NotBlank
    @Size(min = 6, max = 100)
    private String username;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @NotBlank
    private String appType;

    private Set<String> role;
}
