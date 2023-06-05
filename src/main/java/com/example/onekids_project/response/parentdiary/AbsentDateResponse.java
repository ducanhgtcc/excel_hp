package com.example.onekids_project.response.parentdiary;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AbsentDateResponse extends IdResponse {

    private LocalDate absentDate;

    private boolean absentMorning;

    private Boolean morningStatus;

    private boolean absentAfternoon;

    private Boolean afternoonStatus;

    private boolean absentEvening;

    private Boolean eveningStatus;
}
