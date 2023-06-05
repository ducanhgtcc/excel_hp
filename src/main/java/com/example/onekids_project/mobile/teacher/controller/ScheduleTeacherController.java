package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.teacher.response.scheduleclass.ListScheduleFileTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.scheduleclass.ScheduleDateTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.scheduleclass.ScheduleImageWeekTeachResponse;
import com.example.onekids_project.mobile.teacher.response.scheduleclass.ScheduleWeekTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.ScheduleDateTeacherService;
import com.example.onekids_project.mobile.teacher.service.servicecustom.ScheduleImageFileTeacherService;
import com.example.onekids_project.mobile.teacher.service.servicecustom.ScheduleWeekTeacherService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mob/teacher/schedule")
public class ScheduleTeacherController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ScheduleDateTeacherService scheduleDateTeacherService;

    @Autowired
    ScheduleWeekTeacherService scheduleWeekTeacherService;

    @Autowired
    ScheduleImageFileTeacherService scheduleImageFileTeacherService;

    @RequestMapping(method = RequestMethod.GET, value = "/date")
    public ResponseEntity seachSchedulaDate(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        ScheduleDateTeacherResponse scheduleDateTeacherResponse = scheduleDateTeacherService.findScheduleforDay(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataSearch(scheduleDateTeacherResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/month")
    public ResponseEntity searchScheduleMonth(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        List<Integer> dataResponseIntegerList = scheduleDateTeacherService.findClassScheduleMonthList(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataSearch(dataResponseIntegerList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/week")
    public ResponseEntity searchScheduleDate(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        List<ScheduleWeekTeacherResponse> scheduleWeekTeacherResponseList = scheduleWeekTeacherService.findScheduleWeek(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataSearch(scheduleWeekTeacherResponseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/image")
    public ResponseEntity searchImageWeek(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        ScheduleImageWeekTeachResponse scheduleImageWeekTeachResponse = scheduleImageFileTeacherService.findImageWeek(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataSearch(scheduleImageWeekTeachResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/file")
    // không cần truyền vào dữ liệu ngày. Trường hợp ngày, sẽ trả về ngày bé hơn ngày truyền vào để phân trang
    public ResponseEntity searchFileScheduleWeek(@CurrentUser UserPrincipal principal, @Valid PageNumberRequest pageNumberRequest) {
        RequestUtils.getFirstRequest(principal,pageNumberRequest);
        Pageable pageable = PageRequest.of(1, AppConstant.MAX_PAGE_ITEM);
        ListScheduleFileTeacherResponse listScheduleFileTeacherResponse = scheduleImageFileTeacherService.findFileAllWeek(principal, pageNumberRequest.getPageNumber());
        return NewDataResponse.setDataSearch(listScheduleFileTeacherResponse);
    }
}
