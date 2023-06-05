package com.example.onekids_project.response.department;

import com.example.onekids_project.dto.DepartmentDTO;
import com.example.onekids_project.dto.GradeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListDepartmentResponse {
    List<DepartmentDTO> departmentList;
}
