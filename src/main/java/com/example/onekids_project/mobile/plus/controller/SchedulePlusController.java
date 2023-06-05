package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.mobile.plus.request.schedule.ScheduleDatePlusRequest;
import com.example.onekids_project.mobile.plus.response.attendanceKids.AttendanceKidResponse;
import com.example.onekids_project.mobile.plus.response.schedule.ScheduleClassResponse;
import com.example.onekids_project.mobile.plus.response.schedule.ScheduleClassWeekResponse;
import com.example.onekids_project.mobile.plus.response.schedule.ScheduleDatePlusResponse;
import com.example.onekids_project.mobile.plus.response.schedule.ScheduleWeekPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.SchedulePlusService;
import com.example.onekids_project.mobile.request.ScheduleFileRequest;
import com.example.onekids_project.mobile.response.*;
import com.example.onekids_project.mobile.teacher.response.scheduleclass.ScheduleImageWeekTeachResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.schedule.ScheduleService;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/mob/plus/schedule")
public class SchedulePlusController {
    @Autowired
    private SchedulePlusService schedulePlusService;


    /**
     * thống kê dữ liệu TKB các lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-class")
    public ResponseEntity searchScheduleClass(@CurrentUser UserPrincipal principal, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
        RequestUtils.getFirstRequestPlus(principal, localDate);
        List<ScheduleClassResponse> dataList = schedulePlusService.searchScheduleClass(principal, localDate);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * thống kê dữ liệu thời khóa biểu trong ngày
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-schedule-date")
    public ResponseEntity searchScheduleDate(@CurrentUser UserPrincipal principal, @Valid ScheduleDatePlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ScheduleDatePlusResponse dataList = schedulePlusService.searchScheduleDate(principal, request);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * thống kê dữ liệu TKB tháng
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-schedule-month")
    public ResponseEntity searchScheduleMonth(@CurrentUser UserPrincipal principal, @Valid ScheduleDatePlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<Integer> dataList = schedulePlusService.searchScheduleMonth(principal, request);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * thống kê dữ liệu TKB tuần các lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-class-week")
    public ResponseEntity searchScheduleClassWeek(@CurrentUser UserPrincipal principal, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
        RequestUtils.getFirstRequestPlus(principal, localDate);
        List<ScheduleClassWeekResponse> dataList = schedulePlusService.searchScheduleClassWeek(principal, localDate);
        return NewDataResponse.setDataSearch(dataList);

    }

    /**
     * thống kê dữ liệu chi tiết tuần
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-schedule-week")
    public ResponseEntity searchScheduleWeek(@CurrentUser UserPrincipal principal, @Valid ScheduleDatePlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<ScheduleWeekPlusResponse> dataList = schedulePlusService.searchScheduleWeek(principal, request);
        return NewDataResponse.setDataSearch(dataList);
    }


    /**
     * thống kê dữ liệu chi tiết tuần
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-class-file")
    public ResponseEntity searchScheduleFileClass(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        List<FeatureClassResponse> dataList = schedulePlusService.searchScheduleFileClass(principal);
        return NewDataResponse.setDataSearch(dataList);
    }

    /**
     * thống kê dữ liệu ảnh tuần được chọn
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-schedule-image")
    public ResponseEntity searchScheduleImage(@CurrentUser UserPrincipal principal, @Valid ScheduleDatePlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ImageWeekResponse dataList = schedulePlusService.searchScheduleImage(principal, request);
        return NewDataResponse.setDataSearch(dataList);
    }

    /**
     * thống kê dữ liệu file tất cả các tuần
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/statistic-schedule-file")
    public ResponseEntity searchScheduleFile(@CurrentUser UserPrincipal principal, @Valid ScheduleFileRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListFileWeekResponse dataList = schedulePlusService.searchScheduleFile(principal, request);
        return NewDataResponse.setDataSearch(dataList);
    }


}
