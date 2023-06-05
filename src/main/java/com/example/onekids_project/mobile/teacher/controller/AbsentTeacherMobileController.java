package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.teacher.request.absentteacher.AbsentTeacherMobileRequest;
import com.example.onekids_project.mobile.teacher.response.absentteacher.AbsentTeacherDetailMobileResponse;
import com.example.onekids_project.mobile.teacher.response.absentteacher.ListAbsentTeacherMobileResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.AbsentTeacherMobileService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * date 2021-05-22 8:53 AM
 *
 * @author nguyễn văn thụ
 */
@RestController
@RequestMapping(value = "/mob/teacher/absent-teacher")
public class AbsentTeacherMobileController {

    @Autowired
    private AbsentTeacherMobileService absentTeacherMobileServiceService;

    /**
     * Lấy dữ liệu xin nghỉ
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchAbsentTeacher(@CurrentUser UserPrincipal principal, @Valid PageNumberRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataTeacherNoClass(principal);
        ListAbsentTeacherMobileResponse response = absentTeacherMobileServiceService.searchAbsentTeacher(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Chi tiết đơn xin nghỉ
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity showAbsentTeacherDetail(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataTeacherNoClass(principal);
        AbsentTeacherDetailMobileResponse response = absentTeacherMobileServiceService.findAbsentTeacherDetail(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Giáo viên thu hồi (hủy gửi) đơn
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/{id}")
    public ResponseEntity revokeAbsent(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataTeacherNoClass(principal);
        boolean response = absentTeacherMobileServiceService.absentTeacherRevoke(id);
        return NewDataResponse.setDataCustom(response, MessageConstant.REVOKE_SUCCESS);
    }

    /**
     * Tạo đơn xin nghỉ của giáo viên
     *
     * @param principal
     * @param request
     * @return
     * @throws FirebaseMessagingException
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.POST, value = "create")
    public ResponseEntity createAbsentTeacher(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute AbsentTeacherMobileRequest request) throws FirebaseMessagingException, IOException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataTeacherNoClass(principal);
        boolean check = absentTeacherMobileServiceService.createAbsentTeacher(principal, request);
        return NewDataResponse.setDataCustom(check, MessageConstant.ABSENT_SUCCESS);
    }
}
