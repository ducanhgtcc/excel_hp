package com.example.onekids_project.mobile.teacher.request.evaluate;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IdObjectRequest {
    private Long id;

    @NotNull
    private Long idKid;
}
