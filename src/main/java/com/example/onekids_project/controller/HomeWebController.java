package com.example.onekids_project.controller;

import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.home.StatisticalClassHomeResponse;
import com.example.onekids_project.response.home.StatisticalHomeResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.HomeWebService;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/web/home")
public class HomeWebController {
    private static final Logger logger = LoggerFactory.getLogger(HomeWebController.class);

    @Autowired
    private HomeWebService homeWebService;

    @RequestMapping(method = RequestMethod.GET, value = "/statistical")
    public ResponseEntity statisticalInSchool(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        StatisticalHomeResponse response = homeWebService.findStatisticalTotalHome(principal);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/statistical/class")
    public ResponseEntity statisticalInclass(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<StatisticalClassHomeResponse> responseList = homeWebService.findStatisticalClassHome(principal);
        return NewDataResponse.setDataSearch(responseList);
    }
}
