package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.mobile.teacher.response.CameraTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.CameraTeacherService;
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

import java.util.List;

@RestController
@RequestMapping("/mob/teacher/camera")
public class CameraTeacherController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CameraTeacherService cameraTeacherService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity searchCameraClass(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<CameraTeacherResponse> cameraTeacherResponseList = cameraTeacherService.findCameraTeacher(principal);
        return NewDataResponse.setDataSearch(cameraTeacherResponseList);
    }
}
