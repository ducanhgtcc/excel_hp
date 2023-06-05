package com.example.onekids_project.request.attendanceemployee;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * date 2021-03-16 16:48
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class AttendanceEmployeeEatRequest {

    private Long id;

    @NotNull
    private Long idInfo;

    private boolean breakfast;

    private boolean lunch;

    private boolean dinner;

    private boolean isEat;

    @NotNull
    private LocalDate date;
}
