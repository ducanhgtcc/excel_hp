package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.plus.request.UpdatePlusRevokeRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.absentteacher.SearchAbsentTeacherPlusRequest;
import com.example.onekids_project.mobile.plus.response.absent.AbsentPlusConfirmResponse;
import com.example.onekids_project.mobile.plus.response.absent.AbsentPlusDetailResponse;
import com.example.onekids_project.mobile.plus.response.absent.AbsentPlusRevokeResponse;
import com.example.onekids_project.mobile.plus.response.absentteacher.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.AbsentTeacherPlusService;
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

/**
 * date 2021-05-31 8:39 AM
 *
 * @author nguyễn văn thụ
 */
@RestController
@RequestMapping("/mob/plus/absent-teacher")
public class AbsentTeacherPlusController {

    @Autowired
    private AbsentTeacherPlusService absentTeacherPlusService;

    /**
     * Danh sách xin nghỉ teacher
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchMessagePlus(@CurrentUser UserPrincipal principal, @Valid SearchAbsentTeacherPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        CommonValidate.checkDataPlus(principal);
        ListAbsentTeacherPlusResponse response = absentTeacherPlusService.searchAbsentTeacherPlus(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Xem chi tiết
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findMessagePlusDetail(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal);
        CommonValidate.checkDataPlus(principal);
        AbsentTeacherPlusDetailResponse response = absentTeacherPlusService.findDeTailPlusAbsentTeacher(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Thu hồi
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/{id}")
    public ResponseEntity sendRevoke(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        CommonValidate.checkDataPlus(principal);
        AbsentTeacherPlusRevokeResponse response = absentTeacherPlusService.sendRevoke(principal, id);
        return NewDataResponse.setDataRevoke(response);
    }

    /**
     * Xác nhận
     *
     * @param principal
     * @param id
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/confirm/{id}")
    public ResponseEntity confirmSchoolReply(@CurrentUser UserPrincipal principal, @PathVariable Long id) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal);
        CommonValidate.checkDataPlus(principal);
        AbsentTeacherPlusConfirmResponse response = absentTeacherPlusService.absentTeacherPlusConfirm(principal, id);
        return NewDataResponse.setDataCustom(response, MessageConstant.ABSENT_CONFIRM);
    }

    /**
     * Gửi phản hồi
     * @param principal
     * @param request
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/reply")
    public ResponseEntity confirmSchoolReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdatePlusSendReplyRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal);
        CommonValidate.checkDataPlus(principal);
        AbsentTeacherPlusSendReplyResponse response = absentTeacherPlusService.sendPlusReply(principal, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.MESSAGE_SENDREPLY);
    }
}
