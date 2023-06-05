package com.example.onekids_project.response.absentteacher;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * date 2021-05-22 2:32 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AbsentDateTeacherResponse {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private boolean morning;

    private boolean afternoon;

    private boolean evening;
}
