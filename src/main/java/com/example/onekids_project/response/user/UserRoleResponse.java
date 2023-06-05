package com.example.onekids_project.response.user;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.role.RoleOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRoleResponse extends IdResponse {
    private String fullName;

    private String phone;

    private List<RoleOtherResponse> roleList;
}
