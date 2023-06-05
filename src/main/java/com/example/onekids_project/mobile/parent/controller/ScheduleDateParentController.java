package com.example.onekids_project.mobile.parent.controller;


import com.example.onekids_project.mobile.parent.response.scheduleclass.ScheduleDateParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.ScheduleDateParentService;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mob/parent/schedule")
public class ScheduleDateParentController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ScheduleDateParentService scheduleDateParentService;

    @RequestMapping(method = RequestMethod.GET, value = "/date")
    public ResponseEntity searchScheduleDate(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal, dateNotNullRequest);
        CommonValidate.checkDataParent(principal);
        ScheduleDateParentResponse data = scheduleDateParentService.findScheduleforDay(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/month")
    public ResponseEntity searchScheduleMonth(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal, dateNotNullRequest);
        CommonValidate.checkDataParent(principal);
        List<Integer> dataResponse = scheduleDateParentService.findClassScheduleMonthList(principal, dateNotNullRequest.getDate());
        if (dataResponse == null) {
            return NewDataResponse.setDataCustom(dataResponse,"Không có danh sách thời khóa biểu tháng cho phụ huynh");
        }
        return NewDataResponse.setDataSearch(dataResponse);

    }
}
