package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class GradeDTO extends IdDTO {

    private String gradeName;

    private String gradeDescription;

    private int classNumber;

    @JsonBackReference
    private List<MaClassDTO> maClassList;

    private List<MaClassDTO> maClassListResponse;

}
