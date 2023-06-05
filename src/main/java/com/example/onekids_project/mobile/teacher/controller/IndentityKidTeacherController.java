package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.teacher.request.identifykid.IdentityKidRequest;
import com.example.onekids_project.mobile.teacher.response.identitykid.IdentifyKid;
import com.example.onekids_project.mobile.teacher.response.identitykid.InfoIdentityKid;
import com.example.onekids_project.mobile.teacher.service.servicecustom.IdentityKidTeacherService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/mob/teacher/identitykid")
public class IndentityKidTeacherController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    IdentityKidTeacherService identityKidTeacherService;

    @RequestMapping(method = RequestMethod.GET, value = "/class")
    public ResponseEntity getKidsClass(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<IdentifyKid> listKids = identityKidTeacherService.getKidsClass(principal);
        return NewDataResponse.setDataCustom(listKids, MessageConstant.FIND_KID_LIST);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/kid")
    public ResponseEntity getKidIdentity(@CurrentUser UserPrincipal principal, Long idKid) {
        RequestUtils.getFirstRequest(principal);
        List<InfoIdentityKid> infoIdentityKidList = identityKidTeacherService.getKidIdentity(principal, idKid);
        return NewDataResponse.setDataCustom(infoIdentityKidList, MessageConstant.FIND_KID);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity updateDelInsKidIdentity(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid IdentityKidRequest identityKidRequest) throws IOException {
        RequestUtils.getFirstRequest(principal, identityKidRequest);
        identityKidTeacherService.updateDelInsKidIdentity(principal, identityKidRequest);
        return NewDataResponse.setMessage(MessageConstant.CREATE_IDENTIFY);
    }
}
