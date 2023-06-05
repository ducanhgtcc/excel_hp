package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.teacher.response.notifyschool.ListNotifySchoolTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.NotifySchoolTeacherMobileService;
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
 * date 2021-10-22 10:52 AM
 *
 * @author nguyễn văn thụ
 */
@RestController
@RequestMapping("/mob/teacher/notify-school")
public class NotifySchoolTeacherMobileController {

    @Autowired
    private NotifySchoolTeacherMobileService notifySchoolTeacherMobileService;

    /**
     * Tìm kiếm thông báo nhà trường cho giáo viên
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchNotifySchoolTeacher(@CurrentUser UserPrincipal principal, @Valid PageNumberRequest request) {
        RequestUtils.getFirstRequestTeacher(principal);
        ListNotifySchoolTeacherResponse data = notifySchoolTeacherMobileService.searchNotifySchoolTeacher(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }
}
