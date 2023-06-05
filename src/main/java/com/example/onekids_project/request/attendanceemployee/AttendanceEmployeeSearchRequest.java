package com.example.onekids_project.request.attendanceemployee;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * date 2021-03-09 3:18 CH
 *
 * @author ADMIN
 */

@Data
public class AttendanceEmployeeSearchRequest {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    // đã điểm danh, chưa điểm danh, tất cả
    private Boolean status;

    private String nameOrPhone;

}
