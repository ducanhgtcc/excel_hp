package com.example.onekids_project.request.attendancekids;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class AttendanceKidsSearchRequest {
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotNull
    private Long idClass;

    private Boolean absentStatus;

    @Override
    public String toString() {
        return "AttendanceKidsSearchRequest{" +
                "date=" + date +
                ", idClass=" + idClass +
                ", absentStatus=" + absentStatus +
                '}';
    }
}
