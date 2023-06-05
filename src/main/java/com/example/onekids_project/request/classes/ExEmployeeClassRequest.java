package com.example.onekids_project.request.classes;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class ExEmployeeClassRequest {
    @NotNull
    private Long idInfoEmployeeSchool;

    private Boolean isMaster;

    private List<Long> listIdSubject;
}

