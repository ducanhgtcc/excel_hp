package com.example.onekids_project.mobile.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.response.EvaluateSampleMobileResponse;
import com.example.onekids_project.mobile.service.servicecustom.EvaluateMobileService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mob/sample")
public class SampleMobileController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EvaluateMobileService evaluateMobileService;

    @RequestMapping(method = RequestMethod.GET, value = "/evaluate")
    public ResponseEntity findEvaluateSample(@CurrentUser UserPrincipal principal) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName());
        EvaluateSampleMobileResponse data = evaluateMobileService.getEvaluateSample(principal);
        return NewDataResponse.setDataCustom(data, MessageConstant.FIND_EVALUATE_SAMPLE);
    }
}
