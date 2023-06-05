package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.plus.request.UpdatePlusRevokeRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.medicine.SearchMedicinePlusRequest;
import com.example.onekids_project.mobile.plus.response.medicine.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.MedicinePlusService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/mob/plus/medicine")
public class MedicinePlusController {

    @Autowired
    private MedicinePlusService medicinePlusService;

    /**
     * Danh sách dặn thuốc plus
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchMedicinePlus(@CurrentUser UserPrincipal principal, @Valid SearchMedicinePlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListMedicinePlusResponse listMedicinePlusResponse = medicinePlusService.searchMedicinePlus(principal, request);
        return NewDataResponse.setDataSearch(listMedicinePlusResponse);
    }

    /**
     * Chi tiết dặn thuốc
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findMedicinePlusDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        MedicinePlusDetailResponse medicinePlusDetailResponse = medicinePlusService.findDetailMedicinePlus(principal, id);
        return NewDataResponse.setDataCustom(medicinePlusDetailResponse, MessageConstant.SEARCH_DETAIL_MEDICINE);
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
    public ResponseEntity ConfirmReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, id);
        MedicinePlusConfirmResponse medicinePlusConfirmResponse = medicinePlusService.medicinePlusConfirm(principal, id);
        return NewDataResponse.setDataCustom(medicinePlusConfirmResponse, MessageConstant.MEDICINE_CONFIRM);
    }


    /**
     * Gửi phản hồi
     *
     * @param principal
     * @param updatePlusSendReplyRequest
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/reply")
    public ResponseEntity sendReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdatePlusSendReplyRequest updatePlusSendReplyRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, updatePlusSendReplyRequest);
        MedicinePlusSendReplyResponse medicinePlusSendReplyResponse = medicinePlusService.sendPlusReply(principal.getIdSchoolLogin(), principal, updatePlusSendReplyRequest);
        return NewDataResponse.setDataCustom(medicinePlusSendReplyResponse, MessageConstant.MESSAGE_SENDREPLY);
    }

    /**
     * Thu hồi
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/revoke")
    public ResponseEntity sendRevoke(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdatePlusRevokeRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        MedicinePlusRevokeResponse medicinePlusRevokeResponse = medicinePlusService.sendRevoke(principal.getIdSchoolLogin(), principal, request);
        return NewDataResponse.setDataCustom(medicinePlusRevokeResponse, MessageConstant.REVOKE);
    }

}
