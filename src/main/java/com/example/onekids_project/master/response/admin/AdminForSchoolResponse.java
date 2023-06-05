package com.example.onekids_project.master.response.admin;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminForSchoolResponse extends IdResponse {
    private String schoolCode;

    private String schoolName;

    private boolean used;
}
