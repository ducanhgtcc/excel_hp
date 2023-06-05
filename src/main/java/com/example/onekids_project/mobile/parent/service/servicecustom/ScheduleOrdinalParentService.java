package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-05-28 2:05 PM
 *
 * @author nguyễn văn thụ
 */
public interface ScheduleOrdinalParentService {

    List<String> scheduleOrdinalParent(UserPrincipal principal, LocalDate localDate);
}
