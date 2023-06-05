package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.scheduleclass.ScheduleDateParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleDateParentService {

    ScheduleDateParentResponse findScheduleforDay(UserPrincipal principal, LocalDate localDate);

    List<Integer> findClassScheduleMonthList(UserPrincipal principal, LocalDate localDate);

}
