package com.example.onekids_project.request.department;

import com.example.onekids_project.dto.ExDepartmentEmployeeDTO;
import com.example.onekids_project.dto.SchoolDTO;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateDepartmentRequest extends IdRequest {

    private String departmentName;

    private String departmentDescription;

//    private Long idSchool1;

//    private  Long idDepartmentEmployeeList;

    @Override
    public String toString() {
        return "UpdateDepartmentRequest{" +
                "departmentName='" + departmentName + '\'' +
                ", departmentDescription='" + departmentDescription + '\'' +
                "} " + super.toString();
    }
}
