package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.mobile.parent.response.CameraParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.CameraParentService;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mob/parent/camera")
public class CameraParentController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CameraParentService cameraParentService;

    /**
     * find camera for parents
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity searchEvaluateKids(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataParent(principal);
        List<CameraParentResponse> dataList = cameraParentService.findCameraParent(principal);
        return NewDataResponse.setDataSearch(dataList);
    }
}
