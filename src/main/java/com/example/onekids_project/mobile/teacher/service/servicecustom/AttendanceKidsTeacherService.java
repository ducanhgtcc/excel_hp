package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.attendance.AttendanceEatMultiPlusRequest;
import com.example.onekids_project.mobile.plus.request.attendance.AttendanceEatPlusRequest;
import com.example.onekids_project.mobile.plus.response.attendanceKids.AttendanceEatKidClassResponse;
import com.example.onekids_project.mobile.teacher.request.attendacekids.*;
import com.example.onekids_project.mobile.teacher.response.attendancekids.*;
import com.example.onekids_project.response.attendancekids.StatusAttendanceDay;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public interface AttendanceKidsTeacherService {

    AttendanceDayTeacherResponse searchAttendanceKids(UserPrincipal principal, LocalDate localDate);

    List<AttendanceKidsArriveTeacherResponse> searchAttendanceKidsDetail(UserPrincipal principal, LocalDate localDate);

    List<AttendanceKidsArriveTeacherResponse> createAttendanceKidsTeacher(UserPrincipal principal, AttendanceKidArriveTeacherRequest attendanceKidArriveTeacherRequest) throws FirebaseMessagingException;

    AttendanceDayTeacherResponse searchAttendanceKidsLeave(UserPrincipal principal, LocalDate localDate);

    List<AttendanceKidsLeaveTeacherResponse> searchAttendanceKidsDetailLeave(UserPrincipal principal, LocalDate localDate);

    List<AttendanceKidsLeaveTeacherResponse> createAttendanceKidsTeacherLeave(UserPrincipal principal, AttendanceKidLeaveTeacherRequest attendanceKidLeaveTeacherRequest) throws FirebaseMessagingException, IOException;

    AttendanceDayTeacherResponse searchAttendanceKidsEat(UserPrincipal principal, LocalDate localDate);

    List<AttendanceKidEatTeacherResponse> searchAttendanceKidsDetailEat(UserPrincipal principal, LocalDate localDate);

    List<AttendanceKidEatTeacherResponse> createAttendanceKidsTeacherEat(UserPrincipal principal, AttendanceKidEatTeacherRequest attendanceKidEatTeacherRequest);

    List<String> searchAttendanceKidsArriveSample(UserPrincipal principal);

    List<String> searchAttendanceKidsLeaveSample(UserPrincipal principal);

    List<AttendanceKidsArriveTeacherResponse> createAttendanceKidsMultiTeacher(UserPrincipal principal, AttendanceKidArriveTeacherMultiRequest attendanceKidArriveTeacherRequestList) throws FirebaseMessagingException;

    List<AttendanceKidsLeaveTeacherResponse> createAttendanceKidsMultiTeacherLeave(UserPrincipal principal, AttendanceKidLeaveTeacherMultiRequest leaveTeacherMultiRequest) throws FirebaseMessagingException, IOException;

    List<AttendanceKidEatTeacherResponse> createAttendanceKidsMultiTeacherEat(UserPrincipal principal, AttendanceKidEatTeacherMultiRequest attendanceKidEatTeacherMultiRequest);

    AiAttendanceKidsArriveTeacherResponse createAiAttendanceKidsTeacher(UserPrincipal principal, AiAttendanceKidArriveTeacherRequest aiAttendanceKidArriveTeacherRequest) throws FirebaseMessagingException;

    AiAttendanceKidsLeaveTeacherResponse createAiAttendanceKidsTeacherLeave(UserPrincipal principal, AiAttendanceKidLeaveTeacherRequest aiAttendanceKidLeaveTeacherRequest) throws FirebaseMessagingException;

    StatusAttendanceDay checkAttendanceStatusDay(UserPrincipal principal);
}
