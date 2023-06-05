package com.example.onekids_project.controller;

import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.request.commononekids.ChangePhoneSMSRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.commononekids.PlusInSchoolResponse;
import com.example.onekids_project.response.commononekids.SchoolConfigCommonResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.CommonOnekidsService;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/web/common-onekids")
public class CommonOnekidsController {

    private static final Logger logger = LoggerFactory.getLogger(CommonOnekidsController.class);

    @Autowired
    private CommonOnekidsService commonOnekidsService;


    @RequestMapping(method = RequestMethod.PUT, value = "/change-phone-sms")
    public ResponseEntity savePhoneSMS(@CurrentUser UserPrincipal principal, @Valid @RequestBody ChangePhoneSMSRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        boolean check = commonOnekidsService.changePhoneSmsUser(request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.UPDATE_PHONE_SMS);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/school")
    public ResponseEntity searchEmployeeInSchool(@CurrentUser UserPrincipal principal) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName());
        List<PlusInSchoolResponse> responseList = commonOnekidsService.findInforEmployeeInEmployee(principal);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/user/school/{id}")
    public ResponseEntity updateChangeSchool(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), id);
        ResponseEntity response = commonOnekidsService.updateChangeSchool(principal, id);
        return response;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/config/add")
    public ResponseEntity searchConfigCommon(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        SchoolConfigCommonResponse response = commonOnekidsService.getSchoolConfigCommon(principal);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/avatar")
    public ResponseEntity getAvatar(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        String response = commonOnekidsService.getAvatarUser(principal);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * cập nhật lại thông tin trong localStorage
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/api")
    public ResponseEntity getApi(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        Set<String> response = commonOnekidsService.getApiUser(principal);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * cập nhật lại thông tin trường localStorage
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/school")
    public ResponseEntity getSchoolName(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        String response = commonOnekidsService.getSchoolName(principal);
        return NewDataResponse.setDataSearch(response);
    }
}
