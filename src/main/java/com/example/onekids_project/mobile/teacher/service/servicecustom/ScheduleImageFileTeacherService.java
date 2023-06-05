package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.scheduleclass.ListScheduleFileParentResponse;
import com.example.onekids_project.mobile.teacher.response.scheduleclass.ListScheduleFileTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.scheduleclass.ScheduleImageWeekTeachResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ScheduleImageFileTeacherService {
    ScheduleImageWeekTeachResponse findImageWeek(UserPrincipal userPrincipal, LocalDate localDate);

    ListScheduleFileTeacherResponse findFileAllWeek(UserPrincipal userPrincipal, Integer pageNumber);
}
