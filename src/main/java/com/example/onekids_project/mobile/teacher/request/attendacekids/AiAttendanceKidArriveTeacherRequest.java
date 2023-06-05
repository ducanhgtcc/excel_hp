package com.example.onekids_project.mobile.teacher.request.attendacekids;

import com.example.onekids_project.mobile.request.AttendanceStatusDayRequest;
import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;


@Data
public class AiAttendanceKidArriveTeacherRequest extends IdRequest {

    // id kid
    private String content;
    @NotNull
    private MultipartFile picture;

    private AttendanceStatusDayRequest morningList;
    private AttendanceStatusDayRequest afternoonList;
    private AttendanceStatusDayRequest eveningList;

}
