package com.example.onekids_project.dto;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.base.BaseDTO;
import com.example.onekids_project.dto.base.BaseIdDTO;
import com.example.onekids_project.dto.base.IdDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExDepartmentEmployeeDTO extends IdDTO {

    private Long idDepartment;

    private Long idEmployee;

    private String position;

    private  boolean status;

    private  boolean delActive= AppConstant.APP_TRUE;
}
