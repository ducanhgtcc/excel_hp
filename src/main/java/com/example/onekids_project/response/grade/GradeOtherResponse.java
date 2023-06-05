package com.example.onekids_project.response.grade;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GradeOtherResponse extends IdResponse {
    private String gradeName;

    private List<MaClassOtherResponse> maClassList;
}
