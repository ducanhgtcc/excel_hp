package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExEmployeeClassDTO extends IdDTO {

    private boolean isMaster;

    private String listIdSubject;

    private Long idEmployee;

    private EmployeeDTO infoEmployeeSchool;

    private Long idClass;

    private boolean delActive;
}
