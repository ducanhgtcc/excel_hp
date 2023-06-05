package com.example.onekids_project.request.employee;

import com.example.onekids_project.dto.ExEmployeeClassDTO;
import com.example.onekids_project.dto.MaClassDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateExEmployeeClassRequest {
    List<ExEmployeeClassDTO> exEmployeeClassList;
    List<MaClassDTO> maClassList;
}
