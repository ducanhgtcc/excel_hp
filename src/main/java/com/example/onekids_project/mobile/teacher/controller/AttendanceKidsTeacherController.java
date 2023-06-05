package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.teacher.request.attendacekids.*;
import com.example.onekids_project.mobile.teacher.response.attendancekids.*;
import com.example.onekids_project.mobile.teacher.service.servicecustom.AttendanceKidsTeacherService;
import com.example.onekids_project.response.attendancekids.StatusAttendanceDay;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/mob/teacher/attendance")
public class AttendanceKidsTeacherController {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    AttendanceKidsTeacherService attendanceKidsTeacherService;


    /**
     * check status điểm danh của ngày hiện tại
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/attendance-status-day")
    public ResponseEntity checkAttendanceStatus(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        StatusAttendanceDay response = attendanceKidsTeacherService.checkAttendanceStatusDay(principal);
        return NewDataResponse.setDataSearch(response);

    }

    /**
     * thống kê mẫu điểm danh đến
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/sample-arrive")
    public ResponseEntity searchAttendanceKidsArriveSample(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<String> dataList = attendanceKidsTeacherService.searchAttendanceKidsArriveSample(principal);
        return NewDataResponse.setDataSearch(dataList);
    }


    /**
     * thống kê mẫu điểm danh về
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/sample-leave")
    public ResponseEntity searchAttendanceKidsLeaveSample(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<String> dataList = attendanceKidsTeacherService.searchAttendanceKidsLeaveSample(principal);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * thống kê điểm danh đến
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/arrive")
    public ResponseEntity searchAttendanceKidsArrive(@CurrentUser UserPrincipal principal, @RequestParam String localDate) {
        RequestUtils.getFirstRequest(principal);
        LocalDate date = LocalDate.parse(localDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        AttendanceDayTeacherResponse dataList = attendanceKidsTeacherService.searchAttendanceKids(principal, date);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * thống kê điểm danh về
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/leave")
    public ResponseEntity searchAttendanceKidsLeave(@CurrentUser UserPrincipal principal, @RequestParam String localDate) {
        RequestUtils.getFirstRequest(principal);
        LocalDate date = LocalDate.parse(localDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        AttendanceDayTeacherResponse dataList = attendanceKidsTeacherService.searchAttendanceKidsLeave(principal, date);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * thống kê điểm danh ăn
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/eat")
    public ResponseEntity searchAttendanceKidsEat(@CurrentUser UserPrincipal principal, @RequestParam String localDate) {
        RequestUtils.getFirstRequest(principal);
        LocalDate date = LocalDate.parse(localDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        AttendanceDayTeacherResponse dataList = attendanceKidsTeacherService.searchAttendanceKidsEat(principal, date);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * chi tiết điểm danh đến
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail-arrive")
    public ResponseEntity findAttendanceKidsDetailArrive(@CurrentUser UserPrincipal principal, @RequestParam String localDate) {
        RequestUtils.getFirstRequest(principal);
        LocalDate date = LocalDate.parse(localDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<AttendanceKidsArriveTeacherResponse> dataList = attendanceKidsTeacherService.searchAttendanceKidsDetail(principal, date);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * chi tiết điểm danh về
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail-leave")
    public ResponseEntity findAttendanceKidsDetailLeave(@CurrentUser UserPrincipal principal, @RequestParam String localDate) {
        RequestUtils.getFirstRequest(principal);
        LocalDate date = LocalDate.parse(localDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<AttendanceKidsLeaveTeacherResponse> dataList = attendanceKidsTeacherService.searchAttendanceKidsDetailLeave(principal, date);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * chi tiết điểm danh ăn
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail-eat")
    public ResponseEntity findAttendanceKidsDetailEat(@CurrentUser UserPrincipal principal, @RequestParam String localDate) {
        RequestUtils.getFirstRequest(principal);
        LocalDate date = LocalDate.parse(localDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<AttendanceKidEatTeacherResponse> dataList = attendanceKidsTeacherService.searchAttendanceKidsDetailEat(principal, date);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * create điểm danh đến Teacher
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-arrive")
    public ResponseEntity createAttendanceKidsTeacher(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid AttendanceKidArriveTeacherRequest attendanceKidArriveTeacherRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, attendanceKidArriveTeacherRequest);
        List<AttendanceKidsArriveTeacherResponse> dataList = attendanceKidsTeacherService.createAttendanceKidsTeacher(principal, attendanceKidArriveTeacherRequest);
        return NewDataResponse.setDataCreate(dataList);

    }

    /**
     * create điểm danh đến AI Teacher
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-arrive-ai")
    public ResponseEntity createAiAttendanceKidsTeacher(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid AiAttendanceKidArriveTeacherRequest aiAttendanceKidArriveTeacherRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, aiAttendanceKidArriveTeacherRequest);
        AiAttendanceKidsArriveTeacherResponse data = attendanceKidsTeacherService.createAiAttendanceKidsTeacher(principal, aiAttendanceKidArriveTeacherRequest);
        return NewDataResponse.setDataCustom(data, MessageConstant.ATENDANCE_ARRIVE_CREATE);

    }

    /**
     * create điểm danh đến cho nhiều học sinh Teacher
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-multi-arrive")
    public ResponseEntity createAttendanceKidsMultiTeacher(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid AttendanceKidArriveTeacherMultiRequest attendanceKidArriveTeacherRequestList) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, attendanceKidArriveTeacherRequestList);
        List<AttendanceKidsArriveTeacherResponse> dataList = attendanceKidsTeacherService.createAttendanceKidsMultiTeacher(principal, attendanceKidArriveTeacherRequestList);
        return NewDataResponse.setDataCreate(dataList);

    }

    /**
     * create điểm danh về Ai Teacher
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-leave-ai")
    public ResponseEntity createAiAttendanceKidsTeacherLeave(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid AiAttendanceKidLeaveTeacherRequest aiAttendanceKidLeaveTeacherRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, aiAttendanceKidLeaveTeacherRequest);
        AiAttendanceKidsLeaveTeacherResponse data = attendanceKidsTeacherService.createAiAttendanceKidsTeacherLeave(principal, aiAttendanceKidLeaveTeacherRequest);
        return NewDataResponse.setDataCustom(data, MessageConstant.ATENDANCE_LEAVE_CREATE);

    }

    /**
     * create điểm danh về Teacher
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-leave")
    public ResponseEntity createAttendanceKidsTeacherLeave(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid AttendanceKidLeaveTeacherRequest attendanceKidLeaveTeacherRequest) throws FirebaseMessagingException, IOException {
        RequestUtils.getFirstRequest(principal, attendanceKidLeaveTeacherRequest);
        List<AttendanceKidsLeaveTeacherResponse> checkCreate = attendanceKidsTeacherService.createAttendanceKidsTeacherLeave(principal, attendanceKidLeaveTeacherRequest);
        return NewDataResponse.setDataCreate(checkCreate);

    }

    /**
     * create điểm danh về cho nhiều học sinh Teacher
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-multi-leave")
    public ResponseEntity createAttendanceKidsMultiTeacherLeave(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid AttendanceKidLeaveTeacherMultiRequest leaveTeacherMultiRequest) throws FirebaseMessagingException, IOException {
        RequestUtils.getFirstRequest(principal, leaveTeacherMultiRequest);
        List<AttendanceKidsLeaveTeacherResponse> checkCreate = attendanceKidsTeacherService.createAttendanceKidsMultiTeacherLeave(principal, leaveTeacherMultiRequest);
        return NewDataResponse.setDataCreate(checkCreate);

    }

    /**
     * create điểm danh ăn Teacher
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-eat")
    public ResponseEntity createAttendanceKidsTeacherEat(@CurrentUser UserPrincipal principal, @RequestBody @Valid AttendanceKidEatTeacherRequest attendanceKidEatTeacherRequest) {
        RequestUtils.getFirstRequest(principal, attendanceKidEatTeacherRequest);
        List<AttendanceKidEatTeacherResponse> checkCreate = attendanceKidsTeacherService.createAttendanceKidsTeacherEat(principal, attendanceKidEatTeacherRequest);
        return NewDataResponse.setDataCreate(checkCreate);

    }

    /**
     * create điểm danh ăn Teacher
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-multi-eat")
    public ResponseEntity createAttendanceKidsMultiTeacherEat(@CurrentUser UserPrincipal principal, @RequestBody @Valid AttendanceKidEatTeacherMultiRequest attendanceKidEatTeacherMultiRequest) {

        RequestUtils.getFirstRequest(principal, attendanceKidEatTeacherMultiRequest);
        List<AttendanceKidEatTeacherResponse> checkCreate = attendanceKidsTeacherService.createAttendanceKidsMultiTeacherEat(principal, attendanceKidEatTeacherMultiRequest);
        return NewDataResponse.setDataCreate(checkCreate);

    }
}
