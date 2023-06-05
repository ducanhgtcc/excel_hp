package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.teacher.response.phonebook.KidTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.phonebook.ListContactTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.phonebook.ParentTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.ContactTeacherService;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("mob/teacher/phonebook")
public class PhoneBookTeacherController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ContactTeacherService contactTeacherService;

    @RequestMapping(method = RequestMethod.GET, value = "/parent")
    public ResponseEntity seachParentPhonebook(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<ParentTeacherResponse> parentTeacherResponseList = contactTeacherService.findParentPhoneBook(principal);
        return NewDataResponse.setDataCustom(parentTeacherResponseList, MessageConstant.FIND_CONTACT_PARENT);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/kid")
    public ResponseEntity seachKid(@CurrentUser UserPrincipal principal, @Valid Long idKid) {
        RequestUtils.getFirstRequest(principal,idKid);
        KidTeacherResponse kidTeacherResponse = contactTeacherService.findKidPhoneBook(principal, idKid);
        return NewDataResponse.setDataCustom(kidTeacherResponse, MessageConstant.FIND_CONTACT_KID);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/teacher")
    public ResponseEntity seachTeacherPhonebook(@CurrentUser UserPrincipal principal, @Valid PageNumberRequest pageNumberRequest) {
        RequestUtils.getFirstRequest(principal,pageNumberRequest);
        ListContactTeacherResponse data = contactTeacherService.findTeacherPhoneBook(principal, pageNumberRequest);
        return NewDataResponse.setDataCustom(data, MessageConstant.FIND_CONTACT_TEACHER);
    }
}
