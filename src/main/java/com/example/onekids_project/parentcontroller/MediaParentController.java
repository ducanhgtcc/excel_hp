package com.example.onekids_project.parentcontroller;

import com.example.onekids_project.mobile.parent.response.VideoParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.VideoParentService;
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

import java.util.List;

/**
 * @author lavanviet
 */
@RestController
@RequestMapping("web/parent/video")
public class MediaParentController {
    @Autowired
    private VideoParentService videoParentService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity searchEvaluateKids(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataParent(principal);
        VideoParentResponse data = videoParentService.findVideoParent(principal);
        return NewDataResponse.setDataSearch(data);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/other")
    public ResponseEntity searchVideoOther(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataParent(principal);
        List<String> data = videoParentService.findVideoOther(principal);
        return NewDataResponse.setDataSearch(data);
    }
}
