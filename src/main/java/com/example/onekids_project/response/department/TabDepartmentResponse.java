package com.example.onekids_project.response.department;

import com.example.onekids_project.dto.DepartmentDTO;
import com.example.onekids_project.response.base.IdResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TabDepartmentResponse extends IdResponse{

    private String departmentName;
    private Boolean checkDepartment;
    private String position;

}
