package com.example.onekids_project.security.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class JwtMainResponse {
    private String userLogin;

    private String appType;

    private Set<String> role;

    private Set<String> apiSet;
}
