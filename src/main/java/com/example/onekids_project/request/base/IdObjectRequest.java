package com.example.onekids_project.request.base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Data
public class IdObjectRequest {
    @NotNull
    private Long id;
}
