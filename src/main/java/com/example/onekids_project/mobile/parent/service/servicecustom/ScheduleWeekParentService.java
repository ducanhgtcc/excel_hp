package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.scheduleclass.ScheduleWeekParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleWeekParentService {

    List<ScheduleWeekParentResponse> findScheduleWeek(UserPrincipal userPrincipal, LocalDate localDate);

}
