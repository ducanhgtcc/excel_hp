package com.example.onekids_project.mobile.parent.response.absentletter;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AbsentDateDataResponse extends IdResponse {

    private LocalDate absentDate;

    private boolean absentMorning;

    private boolean absentAfternoon;

    private boolean absentEvening;
}
