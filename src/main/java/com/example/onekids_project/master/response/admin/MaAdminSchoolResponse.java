package com.example.onekids_project.master.response.admin;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.school.SchoolOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MaAdminSchoolResponse extends IdResponse {
    private String fullName;

    private String phone;

    private List<SchoolOtherResponse> schoolList;
}
