package com.example.onekids_project.dto;


import com.example.onekids_project.dto.base.BaseDTO;
import com.example.onekids_project.entity.school.ExDepartmentEmployee;
import com.example.onekids_project.entity.school.School;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
public class DepartmentDTO extends BaseDTO<String> {

    private String departmentName;

    private String departmentDescription;

    private SchoolDTO school;

    private List<ExDepartmentEmployeeDTO> departmentEmployeeList;

    private List<ExDepartmentEmployee> exDepartmentEmployees;

    List<Long> idEmployeeInDepartmentList;
    
    int employeeNumber;

    int pageNumber;

    int maxPageItem;
}
