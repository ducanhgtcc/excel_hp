package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.mobile.teacher.response.home.HomeFirstTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.HomeTeacherService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mob/teacher/home-first")
public class HomeFirstTeacherController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HomeTeacherService homeTeacherService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findFirstHome(@CurrentUser UserPrincipal principal) {
        
        HomeFirstTeacherResponse model = homeTeacherService.findHomeFirstTeacher(principal);
        logger.info("Tìm kiếm thông tin chung thành công");
        return NewDataResponse.setData(model, "Tìm kiếm thông tin chung thành công", HttpStatus.OK);
    }

}
