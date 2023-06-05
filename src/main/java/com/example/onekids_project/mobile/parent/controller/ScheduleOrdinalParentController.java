package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.mobile.parent.service.servicecustom.ScheduleOrdinalParentService;
import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * date 2021-05-28 2:04 PM
 *
 * @author nguyễn văn thụ
 */
@RestController
@RequestMapping("/mob/parent/schedule")
public class ScheduleOrdinalParentController {

    @Autowired
    private ScheduleOrdinalParentService scheduleOrdinalParentService;

    /**
     * Lấy thứ tự day, week, file cho mobile
     * @param principal
     * @param dateNotNullRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/ordinal")
    public ResponseEntity ordinalSchedule(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest dateNotNullRequest) {
        RequestUtils.getFirstRequest(principal, dateNotNullRequest);
        CommonValidate.checkDataParent(principal);
        List<String> response = scheduleOrdinalParentService.scheduleOrdinalParent(principal, dateNotNullRequest.getDate());
        return NewDataResponse.setDataSearch(response);
    }
}
