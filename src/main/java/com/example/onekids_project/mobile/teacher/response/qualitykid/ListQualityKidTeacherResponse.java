package com.example.onekids_project.mobile.teacher.response.qualitykid;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ListQualityKidTeacherResponse{
    private List<QualityKidTeacherResponse> dataList;
}
