package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AbsentDateDTO  extends IdDTO {
    private LocalDate absentDate;

    private boolean absentMorning;

    private Boolean morningStatus;

    private boolean absentAfternoon;

    private Boolean afternoonStatus;

    private boolean absentEvening;

    private Boolean eveningStatus;
}
