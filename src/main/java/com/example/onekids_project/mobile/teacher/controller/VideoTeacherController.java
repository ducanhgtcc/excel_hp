package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.mobile.teacher.response.VideoTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.VideoTeacherService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mob/teacher/video")
public class VideoTeacherController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    VideoTeacherService videoTeacherService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity searchVideoTeacher(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        VideoTeacherResponse videoTeacherResponse = videoTeacherService.findVideoTeacher(principal);
        return NewDataResponse.setDataSearch(videoTeacherResponse);
    }
}
