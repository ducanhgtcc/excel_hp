package com.example.onekids_project.mobile.parent.request;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class VaccinateParentRequest extends IdRequest {
    private LocalDate date;

    @NotNull
    private Boolean status;
}
