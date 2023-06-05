package com.example.onekids_project.master.request.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaAdminSearchRequest {
    private String status;

    private String nameOrPhone;

    private Boolean activated;
}
