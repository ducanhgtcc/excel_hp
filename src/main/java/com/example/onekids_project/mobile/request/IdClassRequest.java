package com.example.onekids_project.mobile.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class IdClassRequest {
    @NotNull
    private Long idClass;
}
