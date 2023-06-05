package com.example.onekids_project.mobile.plus.request.attendance;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class AttendanceClassPlusRequest {

    @NotNull
    private Long idClass;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
}
