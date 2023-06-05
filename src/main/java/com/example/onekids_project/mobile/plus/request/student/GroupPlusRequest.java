package com.example.onekids_project.mobile.plus.request.student;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GroupPlusRequest {

    private Long idClass;
    
    @NotNull
    private Long idGroup;
}

