package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.teacher.request.medicine.SearchMedicineTeacherRequest;
import com.example.onekids_project.mobile.teacher.request.medicine.UpdateTeacherReplyMedcRequest;
import com.example.onekids_project.mobile.teacher.response.medicine.*;
import com.example.onekids_project.mobile.teacher.service.servicecustom.MedicineTeacherMobileService;
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

import javax.validation.Valid;

@RestController
@RequestMapping("/mob/teacher/medicine")
public class MedicineTeacherController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    MedicineTeacherMobileService medicineTeacherMobileService;

    /**
     * Danh sách dặn thuốc
     * @param principal
     * @param searchMedicineTeacherRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchmessageTeacher(@CurrentUser UserPrincipal principal, @Valid SearchMedicineTeacherRequest searchMedicineTeacherRequest) {
        RequestUtils.getFirstRequest(principal,searchMedicineTeacherRequest);
        ListMedicineTeacherResponse listMedicineTeacherResponse = medicineTeacherMobileService.searchMedicine(principal, searchMedicineTeacherRequest);
        return NewDataResponse.setDataCustom(listMedicineTeacherResponse, MessageConstant.SEARCH_MEDICINE);
    }

    /**
     * Xem chi tiết
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findMedicineTeacherDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal,id);
        MedicineTeacherDetailResponse medicineTeacherDetailResponse = medicineTeacherMobileService.findDetailMedicineTeacher(principal, id);
        return NewDataResponse.setDataCustom(medicineTeacherDetailResponse, MessageConstant.SEARCH_DETAIL_MEDICINE);
    }

    /**
     * Thu hồi
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/{id}")
    public ResponseEntity revokeTeacherReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal,id);
        MedicineTeacherRevokeResponse medicineTeacherRevokeResponse = medicineTeacherMobileService.medicineTeacherRevoke(principal, id);
        return NewDataResponse.setDataCustom(medicineTeacherRevokeResponse, MessageConstant.MESSAGE_REVOKE);
    }

    /**
     * Gửi phản hồi
     * @param principal
     * @param updateTeacherReplyMedcRequest
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/sendreply")
    public ResponseEntity sendReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateTeacherReplyMedcRequest updateTeacherReplyMedcRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal,updateTeacherReplyMedcRequest);
        MedicineTeacherSendReplyResponse medicineTeacherSendReplyResponse = medicineTeacherMobileService.sendTeacherReply(principal.getIdSchoolLogin(), principal, updateTeacherReplyMedcRequest);
        return NewDataResponse.setDataCustom(medicineTeacherSendReplyResponse, MessageConstant.MESSAGE_SENDREPLY);
    }

    /**
     * Xác nhận
     * @param principal
     * @param id
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/confirm/{id}")
    public ResponseEntity ConfirmTeacherReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal,id);
        MedicineTeacherConfirmResponse medicineTeacherConfirmResponse = medicineTeacherMobileService.medicineTeacherConfirm(principal, id);
        return NewDataResponse.setDataCustom(medicineTeacherConfirmResponse, MessageConstant.MESSAGE_CONFIRM);
    }

    /**
     * Cập nhật
     * @param principal
     * @param updateTeacherReplyMedcRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/updatereply")
    public ResponseEntity updateReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateTeacherReplyMedcRequest updateTeacherReplyMedcRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal,updateTeacherReplyMedcRequest);
        MedicineTeacherSendReplyResponse medicineTeacherSendReplyResponse = medicineTeacherMobileService.updateTeacherReply(principal.getIdSchoolLogin(), principal, updateTeacherReplyMedcRequest);
        return NewDataResponse.setDataCustom(medicineTeacherSendReplyResponse, MessageConstant.MESSAGE_UPDATEREPLY);
    }
}
