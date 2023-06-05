package com.example.onekids_project.mobile.teacher.response.absentteacher;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * date 2021-05-26 9:27 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AbsentTeacherDateMobileResponse {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private boolean morning;

    private boolean afternoon;

    private boolean evening;
}
