package com.example.onekids_project.response.department;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDepartmentResponse extends IdResponse {

    private String departmentName;

    private String departmentDescription;

    private Long idSchool;
}
