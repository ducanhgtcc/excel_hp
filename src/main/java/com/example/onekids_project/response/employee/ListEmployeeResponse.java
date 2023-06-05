package com.example.onekids_project.response.employee;

import com.example.onekids_project.dto.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ListEmployeeResponse {
    List<EmployeeDTO> employeeList;
    Map<String,String> statusEmployee;

}
