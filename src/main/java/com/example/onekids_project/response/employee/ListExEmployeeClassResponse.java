package com.example.onekids_project.response.employee;

import com.example.onekids_project.dto.EmployeeDTO;
import com.example.onekids_project.dto.ExEmployeeClassDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ListExEmployeeClassResponse {
    List<ExEmployeeClassDTO> exEmployeeClassList;
}
