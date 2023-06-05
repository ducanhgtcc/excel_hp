package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.attendanceTeacher.*;
import com.example.onekids_project.mobile.plus.response.attendanceTeacher.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-06-01 3:18 PM
 *
 * @author nguyễn văn thụ
 */
public interface AttendanceTeacherPlusService {

    AttendanceDayTeacherPlusResponse searchAttendanceTeacherArrive(UserPrincipal principal, LocalDate localDate);

    List<AttendanceTeacherArriveResponse> searchAttendanceArriveDetailDay(UserPrincipal principal, LocalDate date);

    StatusAttendanceTeacherDay checkAttendanceStatusTeacherArriveDay(UserPrincipal principal);

    List<AttendanceTeacherArriveResponse> createAttendanceTeacherArrive(UserPrincipal principal, AttendanceTeacherArriveRequest request) throws FirebaseMessagingException, IOException;

    List<AttendanceTeacherArriveResponse> createAttendanceTeacherArriveMany(UserPrincipal principal, ArriveManyRequest requestList) throws FirebaseMessagingException, IOException;

    AttendanceDayTeacherPlusResponse searchAttendanceTeacherLeave(UserPrincipal principal, LocalDate localDate);

    List<AttendanceTeacherLeaveResponse> searchAttendanceLeaveDetailDay(UserPrincipal principal, LocalDate date);

    List<AttendanceTeacherLeaveResponse> createAttendanceTeacherLeave(UserPrincipal principal, AttendanceTeacherLeaveRequest request) throws FirebaseMessagingException, IOException;

    List<AttendanceTeacherLeaveResponse> createAttendanceTeacherLeaveMany(UserPrincipal principal, LeaveManyRequest requestList) throws FirebaseMessagingException, IOException;

    AttendanceDayTeacherPlusResponse searchAttendanceTeacherEat(UserPrincipal principal, LocalDate localDate);

    List<AttendanceTeacherEatResponse> searchAttendanceEatDetailDay(UserPrincipal principal, LocalDate date);

    List<AttendanceTeacherEatResponse> createAttendanceTeacherEat(UserPrincipal principal, AttendanceTeacherEatRequest request) throws FirebaseMessagingException;

    List<AttendanceTeacherEatResponse> createAttendanceTeacherEatMany(UserPrincipal principal, EatManyRequest requestList) throws FirebaseMessagingException;

}
