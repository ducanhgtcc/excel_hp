package com.example.onekids_project.response.employee;

import com.example.onekids_project.response.classes.MaClassOtherResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeClassOtherResponse {
    private boolean isMaster;

    private MaClassOtherResponse maClass;
}
