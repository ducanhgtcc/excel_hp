package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.plus.request.attendance.*;
import com.example.onekids_project.mobile.plus.response.attendanceKids.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.AttendanceKidsPlusService;
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
import java.util.List;


@RestController
@RequestMapping("/mob/plus/attendance")
public class AttendanceKidsPlusController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AttendanceKidsPlusService attendanceKidsPlusService;

    @Autowired
    private AttendanceKidsTeacherService attendanceKidsTeacherService;

    /**
     * thống kê dữ liệu điểm danh theo khối hoạc toàn trường
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-grade")
    public ResponseEntity searchAttendanceGrade(@CurrentUser UserPrincipal principal, @Valid SearchAttendanceGradeRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        AttendanceKidResponse dataList = attendanceKidsPlusService.searchAttendanceGrade(principal, request);
        return NewDataResponse.setDataSearch(dataList);
    }

    /**
     * thống kê dữ liệu điểm danh các lớp theo khối hoạc toàn trường
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-class")
    public ResponseEntity searchAttendanceClass(@CurrentUser UserPrincipal principal, @Valid AttendanceGradePlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<AttendanceKidClassResponse> data = attendanceKidsPlusService.searchAttendanceClass(principal, request);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * thống kê dữ liệu điểm danh đến của lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-arrive")
    public ResponseEntity searchAttendanceArrive(@CurrentUser UserPrincipal principal, @Valid AttendanceClassPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        StatisticAttendanceArriveKid data = attendanceKidsPlusService.searchAttendanceArrive(principal, request);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * thống kê dữ liệu điểm danh về của lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-leave")
    public ResponseEntity searchAttendanceLeave(@CurrentUser UserPrincipal principal, @Valid AttendanceClassPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        StatisticAttendanceLeaveKid data = attendanceKidsPlusService.searchAttendanceLeave(principal, request);
        return NewDataResponse.setDataSearch(data);

    }

    /**
     * thống kê dữ liệu điểm danh ăn của lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-eat")
    public ResponseEntity searchAttendanceEat(@CurrentUser UserPrincipal principal, @Valid AttendanceClassPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        StatisticAttendanceEatKid data = attendanceKidsPlusService.searchAttendanceEat(principal, request);
        return NewDataResponse.setDataSearch(data);

    }

    /**
     * check status điểm danh của ngày hiện tại
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/attendance-status-day")
    public ResponseEntity checkAttendanceStatus(@CurrentUser UserPrincipal principal, @RequestParam Long idClass) {
        RequestUtils.getFirstRequestPlus(principal, idClass);
        StatusAttendanceDay response = attendanceKidsPlusService.checkAttendanceStatusDay(principal, idClass);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Lấy chi tiết điểm danh đến các học sinh
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/data-attendace-arrive")
    public ResponseEntity searchAttendanceArriveKid(@CurrentUser UserPrincipal principal, @Valid AttendanceClassPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<AttendanceArriveKidClassResponse> data = attendanceKidsPlusService.searchAttendanceArriveKid(principal, request);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Lấy chi tiết điểm danh về các học sinh
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/data-attendace-leave")
    public ResponseEntity searchAttendanceLeaveKid(@CurrentUser UserPrincipal principal, @Valid AttendanceClassPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<AttendanceLeaveKidClassResponse> data = attendanceKidsPlusService.searchAttendanceLeaveKid(principal, request);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Lấy chi tiết điểm danh ăn các học sinh
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/data-attendace-eat")
    public ResponseEntity searchAttendanceEatKid(@CurrentUser UserPrincipal principal, @Valid AttendanceClassPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<AttendanceEatKidClassResponse> data = attendanceKidsPlusService.searchAttendanceEatKid(principal, request);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * thống kê mẫu điểm danh đến
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/sample-arrive")
    public ResponseEntity searchAttendanceKidsArriveSample(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
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
        RequestUtils.getFirstRequestPlus(principal);
        List<String> dataList = attendanceKidsTeacherService.searchAttendanceKidsLeaveSample(principal);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * tạo điểm danh đến cho 1 học sinh hoạc điểm danh chung nhiều học sinh
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-attendace-arrive")
    public ResponseEntity createAttendanceArriveKid(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid AttendanceArrivePlusRequest request) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<AttendanceArriveKidClassResponse> data = attendanceKidsPlusService.createAttendanceArriveKid(principal, request);
        return NewDataResponse.setDataCreate(data);
    }

    /**
     * tạo điểm danh về cho 1 học sinh hoạc điểm danh chung nhiều học sinh
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-attendace-leave")
    public ResponseEntity createAttendanceLeaveKid(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid AttendanceLeavePlusRequest request) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<AttendanceLeaveKidClassResponse> data = attendanceKidsPlusService.createAttendanceLeaveKid(principal, request);
        return NewDataResponse.setDataCreate(data);
    }

    /**
     * tạo điểm danh đến cho nhiều học sinh khác nội dung
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-attendace-arrive-multi")
    public ResponseEntity createAttendanceArriveKidMulti(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid ArriveMultiRequest requestList) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, requestList);
        List<AttendanceArriveKidClassResponse> data = attendanceKidsPlusService.createAttendanceArriveKidMulti(principal, requestList);
        return NewDataResponse.setDataCreate(data);
    }

    /**
     * tạo điểm danh về cho 1 học sinh hoạc điểm danh chung nhiều học sinh
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-attendace-leave-multi")
    public ResponseEntity createAttendanceLeaveKidMulti(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid LeaveMultiRequest requestList) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, requestList);
        List<AttendanceLeaveKidClassResponse> data = attendanceKidsPlusService.createAttendanceLeaveKidMulti(principal, requestList);
        return NewDataResponse.setDataCreate(data);
    }

    /**
     * tạo điểm danh đến bằng Ai
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-attendace-arrive-ai")
    public ResponseEntity createAttendanceArriveKidAi(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid AiAttendanceKidArrivePlusRequest requests) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, requests);
        AiAttendanceArriveKidClassResponse data = attendanceKidsPlusService.createAttendanceArriveKidAi(principal, requests);
        if (data.isStatus()) {
            return NewDataResponse.setDataCustom(data, MessageConstant.ATENDANCE_ARRIVE_CREATE);
        }
        return NewDataResponse.setMessage(MessageConstant.ATENDANCE_NO);
    }

    /**
     * tạo điểm danh về bằng Ai
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-attendace-leave-ai")
    public ResponseEntity createAttendanceLeaveKidAi(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid AiAttendanceKidLeavePlusRequest requests) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, requests);
        AiAttendanceLeaveKidClassResponse data = attendanceKidsPlusService.createAttendanceLeaveKidAi(principal, requests);
        if (data.isStatus()) {
            return NewDataResponse.setDataCustom(data, MessageConstant.ATENDANCE_LEAVE_CREATE);
        }
        return NewDataResponse.setMessage(MessageConstant.ATENDANCE_NO);
    }

    /**
     * create điểm danh ăn Teacher
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-eat")
    public ResponseEntity createAttendanceKidsTeacherEat(@CurrentUser UserPrincipal principal, @RequestBody @Valid AttendanceEatPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<AttendanceEatKidClassResponse> checkCreate = attendanceKidsPlusService.createAttendanceEatKid(principal, request);
        return NewDataResponse.setDataCreate(checkCreate);
    }

    /**
     * create điểm danh ăn Teacher
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-multi-eat")
    public ResponseEntity createAttendanceKidsMultiTeacherEat(@CurrentUser UserPrincipal principal, @RequestBody @Valid EatMultiRequest requestList) {
        RequestUtils.getFirstRequestPlus(principal, requestList);
        List<AttendanceEatKidClassResponse> checkCreate = attendanceKidsPlusService.createAttendanceEatKidMulti(principal, requestList);
        return NewDataResponse.setDataCreate(checkCreate);
    }

}
