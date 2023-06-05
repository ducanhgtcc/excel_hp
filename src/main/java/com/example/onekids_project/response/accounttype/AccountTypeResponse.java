package com.example.onekids_project.response.accounttype;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.employee.InfoEmployeeSchoolOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountTypeResponse extends IdResponse {
    private String name;

    private String description;

    List<InfoEmployeeSchoolOtherResponse> infoEmployeeSchoolList;
}
