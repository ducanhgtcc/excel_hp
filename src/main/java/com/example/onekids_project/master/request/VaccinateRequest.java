package com.example.onekids_project.master.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class VaccinateRequest {
    private String yearsOld;

    private String vaccinate;

    private String injectNumber;
}
