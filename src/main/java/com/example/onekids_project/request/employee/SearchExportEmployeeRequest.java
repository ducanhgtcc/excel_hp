package com.example.onekids_project.request.employee;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchExportEmployeeRequest {

    private Long idSchool;

    private String employeeStatus;

    private Long idDepartment;

    private List<Long> list;

}
