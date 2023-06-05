package com.example.onekids_project.response.grade;

import com.example.onekids_project.dto.GradeDTO;
import com.example.onekids_project.response.base.TotalResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListGradeResponse extends TotalResponse {
    List<GradeDTO> gradeList;
}
