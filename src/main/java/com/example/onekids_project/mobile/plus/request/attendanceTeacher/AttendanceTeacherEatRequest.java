package com.example.onekids_project.mobile.plus.request.attendanceTeacher;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-06-04 11:44 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AttendanceTeacherEatRequest {

    @NotNull
    private List<Long> idList;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private boolean breakfast;

    private boolean lunch;

    private boolean dinner;

}
