package com.example.onekids_project.security.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AdminDataRequest {
    @NotBlank
    @Size(max = 255)
    private String fullName;

    @NotBlank
    @Size(min = 6, max = 100)
    private String username;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}
