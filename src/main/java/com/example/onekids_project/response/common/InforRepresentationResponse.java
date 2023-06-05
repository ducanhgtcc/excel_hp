package com.example.onekids_project.response.common;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class InforRepresentationResponse {
    private String fullName;

    private LocalDate birthday;

    private String phone;

    private String email;
}
