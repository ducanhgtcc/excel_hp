package com.example.onekids_project.security.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginSuccessResponse {
    private String token;

    private String avatar;
}
