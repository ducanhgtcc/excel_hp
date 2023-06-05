package com.example.onekids_project.response.commononekids;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoEmployeeSchoolPlusResponse extends IdResponse {

    private String schoolName;

    private boolean status;

}
