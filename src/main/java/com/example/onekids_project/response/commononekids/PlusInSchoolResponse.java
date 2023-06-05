package com.example.onekids_project.response.commononekids;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.school.SchoolOtherResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlusInSchoolResponse extends IdResponse {

    private String fullName;

    private String phone;

    private InfoEmployeeSchoolPlusResponse school;

}
