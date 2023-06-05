package com.example.onekids_project.mobile.plus.request.attendanceTeacher;

import com.example.onekids_project.mobile.request.AttendanceStatusDayRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-06-09 9:50 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AttendanceTeacherArriveManyRequest {

    @NotNull
    private Long id;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private String content;

    private MultipartFile picture;

    private boolean deletePicture;

    private String time;

    private AttendanceStatusDayRequest attendanceMorning;

    private AttendanceStatusDayRequest attendanceAfternoon;

    private AttendanceStatusDayRequest attendanceEvening;

    @Override
    public String toString() {
        return "AttendanceOneKidArriveTeacherRequest{" +
                "content='" + content + '\'' +
                ", picture=" + picture +
                ", deletePicture=" + deletePicture +
                ", time='" + time + '\'' +
                ", attendanceMorning=" + attendanceMorning +
                ", attendanceAfternoon=" + attendanceAfternoon +
                ", attendanceEvening=" + attendanceEvening +
                '}';
    }
}
