package com.example.onekids_project.mobile.plus.request.attendance;

import com.example.onekids_project.mobile.request.AttendanceStatusDayRequest;
import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AiAttendanceKidArrivePlusRequest extends IdRequest {

    private Long idClass;

    // id kid

    private String content;
    
    private MultipartFile picture;

    private AttendanceStatusDayPlusRequest morningList;

    private AttendanceStatusDayPlusRequest afternoonList;

    private AttendanceStatusDayPlusRequest eveningList;
}
