package com.example.onekids_project.mobile.teacher.request.absentteacher;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * date 2021-05-21 5:13 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AbsentDateRequest {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private boolean morning;

    private boolean afternoon;

    private boolean evening;
}
