package com.example.onekids_project.response.employee;

import com.example.onekids_project.dto.EmployeeDTO;
import com.example.onekids_project.dto.InfoEmployeeSchoolDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ListInfoEmployeeSchoolResponse {
    List<InfoEmployeeSchoolDTO> infoEmployeeSchoolDTOList;
}
