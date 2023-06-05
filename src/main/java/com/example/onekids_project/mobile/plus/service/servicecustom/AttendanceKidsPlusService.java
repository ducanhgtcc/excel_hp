package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.attendance.*;
import com.example.onekids_project.mobile.plus.response.attendanceKids.*;
import com.example.onekids_project.response.attendancekids.StatusAttendanceDay;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceKidsPlusService {
    List<AttendanceKidClassResponse> searchAttendanceClass(UserPrincipal principal, AttendanceGradePlusRequest attendanceGradePlusRequest);

    AttendanceKidResponse searchAttendanceGrade(UserPrincipal principal, SearchAttendanceGradeRequest request);

    StatisticAttendanceArriveKid searchAttendanceArrive(UserPrincipal principal, AttendanceClassPlusRequest request);

    List<AttendanceArriveKidClassResponse> searchAttendanceArriveKid(UserPrincipal principal, AttendanceClassPlusRequest request);

    List<AttendanceArriveKidClassResponse> createAttendanceArriveKid(UserPrincipal principal, AttendanceArrivePlusRequest arrivePlusRequest) throws IOException, FirebaseMessagingException;

    List<AttendanceArriveKidClassResponse> createAttendanceArriveKidMulti(UserPrincipal principal, ArriveMultiRequest requests) throws IOException, FirebaseMessagingException;

    AiAttendanceArriveKidClassResponse createAttendanceArriveKidAi(UserPrincipal principal, AiAttendanceKidArrivePlusRequest requests) throws IOException, FirebaseMessagingException;

    StatisticAttendanceLeaveKid searchAttendanceLeave(UserPrincipal principal, AttendanceClassPlusRequest request);

    List<AttendanceLeaveKidClassResponse> searchAttendanceLeaveKid(UserPrincipal principal, AttendanceClassPlusRequest request);

    List<AttendanceLeaveKidClassResponse> createAttendanceLeaveKid(UserPrincipal principal, AttendanceLeavePlusRequest request) throws IOException, FirebaseMessagingException;

    List<AttendanceLeaveKidClassResponse> createAttendanceLeaveKidMulti(UserPrincipal principal, LeaveMultiRequest requestList) throws IOException, FirebaseMessagingException;

    AiAttendanceLeaveKidClassResponse createAttendanceLeaveKidAi(UserPrincipal principal, AiAttendanceKidLeavePlusRequest requests) throws IOException, FirebaseMessagingException;

    StatisticAttendanceEatKid searchAttendanceEat(UserPrincipal principal, AttendanceClassPlusRequest request);

    List<AttendanceEatKidClassResponse> searchAttendanceEatKid(UserPrincipal principal, AttendanceClassPlusRequest request);

    List<AttendanceEatKidClassResponse> createAttendanceEatKid(UserPrincipal principal, AttendanceEatPlusRequest request);

    List<AttendanceEatKidClassResponse> createAttendanceEatKidMulti(UserPrincipal principal, EatMultiRequest requestList);

    StatusAttendanceDay checkAttendanceStatusDay(UserPrincipal principal, Long idClass);
}
