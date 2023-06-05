package com.example.onekids_project.response.department;

import com.example.onekids_project.dto.ExDepartmentEmployeeDTO;
import com.example.onekids_project.dto.SchoolDTO;
import com.example.onekids_project.entity.school.ExDepartmentEmployee;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.response.base.IdResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateDepartmentResponse extends IdResponse {

    private String departmentName;

    private String departmentDescription;

    private Long idSchool;
}
