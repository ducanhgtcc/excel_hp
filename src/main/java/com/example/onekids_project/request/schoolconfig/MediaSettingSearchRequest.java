package com.example.onekids_project.request.schoolconfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaSettingSearchRequest {
    private Long idGrade;

    private Long idClass;

    private String className;
}
