package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.mobile.parent.request.absent.AbsentMobileRequest;
import com.example.onekids_project.mobile.parent.response.absentletter.AbsentLetterDetailMobileResponse;
import com.example.onekids_project.mobile.parent.response.absentletter.ListAbsentLetterMobileResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.AbsentLetterMobileService;
import com.example.onekids_project.mobile.request.DateTimeRequest;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/mob/parent/absentletter")
public class AbsentLetterMobileController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AbsentLetterMobileService absentLetterMobileService;

    /**
     * tìm kiếm danh sách lời nhắn
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity searchAbsentLetter(@CurrentUser UserPrincipal principal, DateTimeRequest localDateTime) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataParent(principal);
        Pageable pageable = PageRequest.of(0, AppConstant.MAX_PAGE_ITEM);
        ListAbsentLetterMobileResponse listAbsentLetterMobileResponse = absentLetterMobileService.findAbsentMoblie(principal, pageable, localDateTime.getDateTime());
        return NewDataResponse.setDataSearch(listAbsentLetterMobileResponse);

    }


    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/{id}")
    public ResponseEntity revokeAbsentLetter(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal);
        CommonValidate.checkDataParent(principal);
        boolean checkUpdate = absentLetterMobileService.absentRevoke(id);
        return NewDataResponse.setDataCustom(checkUpdate, "Thu hồi xin nghỉ thành công");

    }

    /**
     * xem chi tiết xin nghỉ
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findAbsentDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        AbsentLetterDetailMobileResponse absentLetterDetailMobileResponse = absentLetterMobileService.findAbsentDetailMobile(principal, id);
        return NewDataResponse.setDataSearch(absentLetterDetailMobileResponse);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity createAbsentLetter(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute AbsentMobileRequest absentMobileRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, absentMobileRequest);
        CommonValidate.checkDataParent(principal);
        absentLetterMobileService.createAbsent(principal, absentMobileRequest);
        return NewDataResponse.setDataCustom("Tạo xin nghỉ thành công", "Tạo xin nghỉ thành công");

    }

}
