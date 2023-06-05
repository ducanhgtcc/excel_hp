package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class ParentDTO extends IdDTO {
    private String parentCode;

    private String accountType;

    private String fatherAvatar;

    private String representation;

    private String fatherName;

    private LocalDate fatherBirthday;

    private String fatherPhone;

    private String fatherEmail;

    private String fatherGender;

    private String fatherJob;

    private String motherAvatar;

    private String motherName;

    private LocalDate motherBirthday;

    private String motherPhone;

    private String motherEmail;

    private String motherGender;

    private String motherJob;

    private Long idKidLogin;

    private List<KidsDTO> kidsListResponse;
}
