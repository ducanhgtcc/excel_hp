package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.teacher.request.notifyTeacher.CreateNotifyTeacherRequest;
import com.example.onekids_project.mobile.teacher.request.notifyTeacher.SearchNotifyTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.notify.ListMobileNotifyTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.notify.MobileNotifiDetailTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.NotifyTeacherService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/mob/teacher/notify")
public class NotifyTeacherController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    NotifyTeacherService notifyTeacherService;

    /**
     * tìm kiếm thông báo cho giáo viên
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity searchNotifiOfTeacher(@CurrentUser UserPrincipal principal, SearchNotifyTeacherRequest searchNotifyTeacherRequest) {
        RequestUtils.getFirstRequest(principal, searchNotifyTeacherRequest);
        ListMobileNotifyTeacherResponse listMobileNotifiTeacherResponse = notifyTeacherService.findNotifiTeacherForMobile(principal, searchNotifyTeacherRequest);
        return NewDataResponse.setDataSearch(listMobileNotifiTeacherResponse);

    }

    /**
     * xem chi tiết thông báo của giáo viên
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity showNotifiDetailOfTeacher(@PathVariable("id") Long id, @CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal, id);
        MobileNotifiDetailTeacherResponse mobileNotifiDetailTeacherResponse = notifyTeacherService.findNotifiTeacherByIdForMobile(id, principal);
        return NewDataResponse.setDataSearch(mobileNotifiDetailTeacherResponse);
    }

    /**
     * gửi thông báo từ teacher tới phụ huynh
     *
     * @param createNotifyTeacherRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity createNotififorTeacherToParent(@CurrentUser UserPrincipal principal, @ModelAttribute CreateNotifyTeacherRequest createNotifyTeacherRequest) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, createNotifyTeacherRequest);
        boolean checkCreate = notifyTeacherService.createNotififorTeacherToParent(principal, createNotifyTeacherRequest);
        return NewDataResponse.setDataCustom(checkCreate, MessageConstant.CREATE_NOTIFY);
    }
}
