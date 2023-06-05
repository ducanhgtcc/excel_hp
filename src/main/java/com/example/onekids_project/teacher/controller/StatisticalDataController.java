package com.example.onekids_project.teacher.controller;

import com.example.onekids_project.mobile.request.DateNotNullRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.teacher.response.statistical.StatisticalDataResponse;
import com.example.onekids_project.teacher.service.servicecustom.StatisticalService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * date 2021-04-19 09:50
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/web/teacher/common")
public class StatisticalDataController {
    @Autowired
    private StatisticalService statisticalService;

    @RequestMapping(method = RequestMethod.GET, value = {"/statistical"})
    public ResponseEntity searchMessage(@CurrentUser UserPrincipal principal, @Valid DateNotNullRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        StatisticalDataResponse response = statisticalService.getStatisticalData(principal, request.getDate());
        return NewDataResponse.setDataSearch(response);
    }
}
