package com.example.onekids_project.mobile.plus.response.department;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentPlusResponse extends IdResponse {

    private String departmentName;

    private int numberEmployees;

}
