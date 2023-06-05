package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.teacher.response.scheduleclass.ScheduleDateTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleDateTeacherService {
    ScheduleDateTeacherResponse findScheduleforDay(UserPrincipal principal, LocalDate localDate);

    List<Integer> findClassScheduleMonthList(UserPrincipal principal, LocalDate localDate);


}
