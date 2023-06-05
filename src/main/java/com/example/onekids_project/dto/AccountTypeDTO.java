package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTypeDTO extends IdDTO {

    private String name;

    private String description;

    private Long idSchool;

}
