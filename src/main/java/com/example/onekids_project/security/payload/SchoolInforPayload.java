package com.example.onekids_project.security.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolInforPayload {

    private String schoolName;

    // nhiều trường, nhiều lớp, nhiều học sinh để hiện đổi trường, lớp và học sinh trên giao diện web
    private boolean manyStatus;
}
