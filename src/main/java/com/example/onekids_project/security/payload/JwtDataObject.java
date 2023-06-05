package com.example.onekids_project.security.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class JwtDataObject {

    private LocalDate key;

    private String value;

    private boolean status;
}
