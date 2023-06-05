package com.example.onekids_project.response.attendanceemployee;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Data;

import java.time.LocalDate;

/**
 * date 2021-03-15 13:39
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class AttendanceEatEmployeeDateResponse extends IdResponse {

    private Long idInfo;

    private boolean breakfast;

    private boolean lunch;

    private boolean dinner;

    private boolean isEat;

    private LocalDate date;
}
