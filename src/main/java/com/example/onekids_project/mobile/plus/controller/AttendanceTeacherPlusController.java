package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.mobile.plus.request.attendanceTeacher.*;
import com.example.onekids_project.mobile.plus.response.attendanceTeacher.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.AttendanceTeacherPlusService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * date 2021-06-01 4:20 PM
 *
 * @author nguyễn văn thụ
 */
@RestController
@RequestMapping("/mob/plus/attendance-teacher")
public class AttendanceTeacherPlusController {

    @Autowired
    private AttendanceTeacherPlusService attendanceTeacherPlusService;

    /**
     * check status điểm danh của ngày hiện tại
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/attendance-status-day")
    public ResponseEntity checkAttendanceStatus(@CurrentUser UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        RequestUtils.getFirstRequest(principal);
        StatusAttendanceTeacherDay response = attendanceTeacherPlusService.checkAttendanceStatusTeacherArriveDay(principal);
        return NewDataResponse.setDataSearch(response);
    }
    /**
     * thống kê điểm danh đến
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/arrive")
    public ResponseEntity searchAttendanceTeacherArrive(@CurrentUser UserPrincipal principal, @RequestParam String date) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        AttendanceDayTeacherPlusResponse dataList = attendanceTeacherPlusService.searchAttendanceTeacherArrive(principal, localDate);
        return NewDataResponse.setDataSearch(dataList);
    }

    /**
     * Lấy DL Chấm công đến, thay đổi ngày được chọn
     * @param principal
     * @param date
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail-arrive")
    public ResponseEntity findAttendanceTeacherDetailArrive(@CurrentUser UserPrincipal principal, @RequestParam String date) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<AttendanceTeacherArriveResponse> dataList = attendanceTeacherPlusService.searchAttendanceArriveDetailDay(principal, localDate);
        return NewDataResponse.setDataSearch(dataList);
    }

    /**
     * create điểm danh đến 1 or cùng nội dung
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-arrive")
    public ResponseEntity createAttendanceTeacherArrive(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid AttendanceTeacherArriveRequest request) throws FirebaseMessagingException, IOException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<AttendanceTeacherArriveResponse> data = attendanceTeacherPlusService.createAttendanceTeacherArrive(principal, request);
        return NewDataResponse.setDataCreate(data);
    }

    /**
     * create điểm danh đến all
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-arrive/many")
    public ResponseEntity createAttendanceTeacherArrive(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid ArriveManyRequest request) throws FirebaseMessagingException, IOException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<AttendanceTeacherArriveResponse> dataList = attendanceTeacherPlusService.createAttendanceTeacherArriveMany(principal, request);
        return NewDataResponse.setDataCreate(dataList);
    }

    /**
     * thống kê điểm danh về
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/leave")
    public ResponseEntity searchAttendanceTeacherLeave(@CurrentUser UserPrincipal principal, @RequestParam String date) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        AttendanceDayTeacherPlusResponse dataList = attendanceTeacherPlusService.searchAttendanceTeacherLeave(principal, localDate);
        return NewDataResponse.setDataSearch(dataList);
    }

    /**
     * Lấy DL Chấm công về, thay đổi ngày được chọn
     * @param principal
     * @param date
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail-leave")
    public ResponseEntity findAttendanceTeacherDetailLeave(@CurrentUser UserPrincipal principal, @RequestParam String date) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<AttendanceTeacherLeaveResponse> dataList = attendanceTeacherPlusService.searchAttendanceLeaveDetailDay(principal, localDate);
        return NewDataResponse.setDataSearch(dataList);
    }

    /**
     * create điểm danh về
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-leave")
    public ResponseEntity createAttendanceTeacherLeave(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid AttendanceTeacherLeaveRequest request) throws FirebaseMessagingException, IOException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<AttendanceTeacherLeaveResponse> data = attendanceTeacherPlusService.createAttendanceTeacherLeave(principal, request);
        return NewDataResponse.setDataCreate(data);
    }

    /**
     * create điểm danh về many
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-leave/many")
    public ResponseEntity createAttendanceTeacherLeaveMany(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid LeaveManyRequest request) throws FirebaseMessagingException, IOException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<AttendanceTeacherLeaveResponse> data = attendanceTeacherPlusService.createAttendanceTeacherLeaveMany(principal, request);
        return NewDataResponse.setDataCreate(data);
    }

    /**
     * thống kê điểm danh ăn
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/eat")
    public ResponseEntity searchAttendanceTeacherEat(@CurrentUser UserPrincipal principal, @RequestParam String date) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        AttendanceDayTeacherPlusResponse dataList = attendanceTeacherPlusService.searchAttendanceTeacherEat(principal, localDate);
        return NewDataResponse.setDataSearch(dataList);
    }

    /**
     * Lấy DL Chấm điểm danh ăn, thay đổi ngày được chọn
     * @param principal
     * @param date
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail-eat")
    public ResponseEntity findAttendanceTeacherDetailEat(@CurrentUser UserPrincipal principal, @RequestParam String date) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<AttendanceTeacherEatResponse> dataList = attendanceTeacherPlusService.searchAttendanceEatDetailDay(principal, localDate);
        return NewDataResponse.setDataSearch(dataList);
    }

    /**
     * create điểm danh ăn
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-eat")
    public ResponseEntity createAttendanceTeacherEat(@CurrentUser UserPrincipal principal, @RequestBody @Valid AttendanceTeacherEatRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<AttendanceTeacherEatResponse> data = attendanceTeacherPlusService.createAttendanceTeacherEat(principal, request);
        return NewDataResponse.setDataCreate(data);
    }

    /**
     * create điểm danh ăn many
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-eat/many")
    public ResponseEntity createAttendanceTeacherLeaveMany(@CurrentUser UserPrincipal principal, @RequestBody @Valid EatManyRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        List<AttendanceTeacherEatResponse> data = attendanceTeacherPlusService.createAttendanceTeacherEatMany(principal, request);
        return NewDataResponse.setDataCreate(data);
    }
}
