package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.mobile.parent.response.notifyschool.ListNotifySchoolParentResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.NotifySchoolParentMobileService;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * date 2021-10-22 9:58 AM
 *
 * @author nguyễn văn thụ
 */
@RestController
@RequestMapping("/mob/parent/notify-school")
public class NotifySchoolParentMobileController {

    @Autowired
    private NotifySchoolParentMobileService notifySchoolParentMobileService;

    /**
     * Tìm kiếm thông báo nhà trường cho phụ huynh
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchNotifySchoolParent(@CurrentUser UserPrincipal principal, @Valid PageNumberRequest request) {
        RequestUtils.getFirstRequestParent(principal);
        ListNotifySchoolParentResponse data = notifySchoolParentMobileService.searchNotifySchool(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }
}
