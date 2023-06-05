package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.schedule.ScheduleDatePlusRequest;
import com.example.onekids_project.mobile.plus.response.schedule.ScheduleClassResponse;
import com.example.onekids_project.mobile.plus.response.schedule.ScheduleClassWeekResponse;
import com.example.onekids_project.mobile.plus.response.schedule.ScheduleDatePlusResponse;
import com.example.onekids_project.mobile.plus.response.schedule.ScheduleWeekPlusResponse;
import com.example.onekids_project.mobile.request.ScheduleFileRequest;
import com.example.onekids_project.mobile.response.FeatureClassResponse;
import com.example.onekids_project.mobile.response.FileWeekResponse;
import com.example.onekids_project.mobile.response.ImageWeekResponse;
import com.example.onekids_project.mobile.response.ListFileWeekResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

public interface SchedulePlusService {
    List<ScheduleClassResponse> searchScheduleClass(UserPrincipal principal, LocalDate localDate);

    ScheduleDatePlusResponse searchScheduleDate(UserPrincipal principal, ScheduleDatePlusRequest request);

    List<Integer> searchScheduleMonth(UserPrincipal principal, ScheduleDatePlusRequest request);

    List<ScheduleClassWeekResponse> searchScheduleClassWeek(UserPrincipal principal, LocalDate localDate);

    List<ScheduleWeekPlusResponse> searchScheduleWeek(UserPrincipal principal, ScheduleDatePlusRequest request);

    List<FeatureClassResponse> searchScheduleFileClass(UserPrincipal principal);

    ImageWeekResponse searchScheduleImage(UserPrincipal principal, ScheduleDatePlusRequest request);

    ListFileWeekResponse searchScheduleFile(UserPrincipal principal, ScheduleFileRequest request);
}
