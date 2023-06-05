package com.example.onekids_project.mobile.parent.response.kids;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VaccinateParentResponse extends IdResponse {
    private String yearsOld;

    private String vaccinate;

    private String injectNumber;

    private boolean status;

    private LocalDate date;

}
