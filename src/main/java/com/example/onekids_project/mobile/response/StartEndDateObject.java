package com.example.onekids_project.mobile.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StartEndDateObject {
    private LocalDate startDate;

    private LocalDate endDate;
}
