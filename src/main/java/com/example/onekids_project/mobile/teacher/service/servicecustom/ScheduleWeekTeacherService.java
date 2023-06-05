package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.teacher.response.scheduleclass.ScheduleWeekTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleWeekTeacherService {
    List<ScheduleWeekTeacherResponse> findScheduleWeek(UserPrincipal userPrincipal, LocalDate localDate);
}
