package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.teacher.response.menuclass.ListMenuFileTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.menuclass.MenuDateTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.menuclass.MenuImageWeekTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.menuclass.MenuWeekTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.ClassMenuDateTeacherService;
import com.example.onekids_project.mobile.teacher.service.servicecustom.ClassMenuImageFileTeacherService;
import com.example.onekids_project.mobile.teacher.service.servicecustom.ClassMenuWeekTeacherService;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mob/teacher/menu")
public class MenuTeacherController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ClassMenuDateTeacherService classMenuDateTeacherService;

    @Autowired
    private ClassMenuWeekTeacherService classMenuWeekTeacherService;

    @Autowired
    private ClassMenuImageFileTeacherService classMenuImageFileTeacherService;

    @RequestMapping(method = RequestMethod.GET, value = "/date")
    public ResponseEntity searchScheduleDate(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        List<MenuDateTeacherResponse> menuDateTeacherResponseList = classMenuDateTeacherService.findDateMenu(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataSearch(menuDateTeacherResponseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/month")
    public ResponseEntity searchMenuMonth(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        List<Integer> dataResponseList = classMenuDateTeacherService.findClassMenuMonthList(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataSearch(dataResponseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/week")
    public ResponseEntity searchMenuWeek(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        List<MenuWeekTeacherResponse> menuWeekTeacherResponseList = classMenuWeekTeacherService.findWeekMenu(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataSearch(menuWeekTeacherResponseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/image-week")
    public ResponseEntity searchImageMenuWeek(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal,dateNotNullRequest);
        MenuImageWeekTeacherResponse menuImageWeekTeacherResponse = classMenuImageFileTeacherService.findImageWeek(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataSearch(menuImageWeekTeacherResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/file-week")
    // không cần truyền vào dữ liệu ngày. Trường hợp ngày, sẽ trả về ngày bé hơn ngày truyền vào để phân trang
    public ResponseEntity searchFileMenuWeek(@CurrentUser UserPrincipal principal, @Valid PageNumberRequest pageNumberRequest) {
        RequestUtils.getFirstRequest(principal,pageNumberRequest);
        ListMenuFileTeacherResponse listMenuFileTeacherResponse = classMenuImageFileTeacherService.findFileAllWeek(principal, pageNumberRequest.getPageNumber());
        return NewDataResponse.setDataSearch(listMenuFileTeacherResponse);
    }
}
