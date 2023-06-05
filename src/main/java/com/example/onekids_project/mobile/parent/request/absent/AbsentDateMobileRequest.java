package com.example.onekids_project.mobile.parent.request.absent;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class AbsentDateMobileRequest {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate absentDate;

    private boolean absentMorning;

    private boolean absentAfternoon;

    private boolean absentEvening;
}
