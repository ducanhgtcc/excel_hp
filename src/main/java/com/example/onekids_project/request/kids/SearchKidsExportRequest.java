package com.example.onekids_project.request.kids;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class SearchKidsExportRequest {

    private Long idSchool;

    private Long idGrade;

    private Long idGroup;

    private Long idClass;

    private List<Long> idKidsList;

    private  String status;
}
