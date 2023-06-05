package com.example.onekids_project.response.employee;

import com.example.onekids_project.dto.ExEmployeeClassDTO;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateExEmployeeClassResponse extends IdResponse {
    List<ExEmployeeClassDTO> exEmployeeClassList;
}
