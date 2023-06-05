package com.example.onekids_project.response.user;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleForUserResponse extends IdResponse {
    private String roleName;

    private String description;

    private boolean used;
}
