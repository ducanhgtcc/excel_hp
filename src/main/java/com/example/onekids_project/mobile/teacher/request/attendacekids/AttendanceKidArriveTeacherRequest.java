package com.example.onekids_project.mobile.teacher.request.attendacekids;

import com.example.onekids_project.mobile.request.AttendanceStatusDayRequest;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AttendanceKidArriveTeacherRequest {

    @NotNull
    private List<Long> idAttendace;

    private String content;

    private MultipartFile picture;

    private boolean deletePicture;

    private String time;

    private AttendanceStatusDayRequest morningList;
    private AttendanceStatusDayRequest afternoonList;
    private AttendanceStatusDayRequest eveningList;

}
