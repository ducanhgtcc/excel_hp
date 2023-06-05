package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.scheduleclass.ListScheduleFileParentResponse;
import com.example.onekids_project.mobile.parent.response.scheduleclass.ScheduleImageParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ScheduleImageFileParentService {
    ScheduleImageParentResponse findImageWeek(UserPrincipal userPrincipal, LocalDate localDate);

    ListScheduleFileParentResponse findFileAllWeek(UserPrincipal userPrincipal, Pageable pageable, LocalDate localDate);
}
