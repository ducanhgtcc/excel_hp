package com.example.onekids_project.mobile.controller;

import com.example.onekids_project.mobile.response.onecam.ParentCamResponse;
import com.example.onekids_project.mobile.response.onecam.PlusCamResponse;
import com.example.onekids_project.mobile.response.onecam.TeacherCamResponse;
import com.example.onekids_project.mobile.service.OneCamService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lavanviet
 */
@RestController
@RequestMapping("/one-came")
public class OneCamController {

    @Autowired
    private OneCamService oneCamService;

    @RequestMapping(method = RequestMethod.GET, value = "/parent/list")
    public ResponseEntity getParentCamList(@CurrentUser UserPrincipal principal, @RequestParam String idDevice) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataParent(principal);
        List<ParentCamResponse> responseList = oneCamService.getCameraParentList(idDevice);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/teacher/list")
    public ResponseEntity getTeacherCamList(@CurrentUser UserPrincipal principal, @RequestParam String idDevice) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataTeacher(principal);
        List<TeacherCamResponse> responseList = oneCamService.getCameraTeacherList(idDevice);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/plus/list")
    public ResponseEntity getPlusCamList(@CurrentUser UserPrincipal principal, @RequestParam String idDevice) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataPlus(principal);
        List<PlusCamResponse> responseList = oneCamService.getCameraPlusList(idDevice);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/logout")
    public ResponseEntity logoutCameraController(@CurrentUser UserPrincipal principal, @RequestParam String idDevice) {
        RequestUtils.getFirstRequest(principal);
        oneCamService.logoutCameraService(idDevice);
        return NewDataResponse.setDataUpdate(true);
    }
}
