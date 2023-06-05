package com.example.onekids_project.controller.historySms;

import com.example.onekids_project.mobile.teacher.response.historynotifi.SmsCustomResponse;
import com.example.onekids_project.request.notifihistory.SearchSmsSendCustomRequest;
import com.example.onekids_project.request.smsNotify.SmsCustomRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.response.notifihistory.ListSmsCustomResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.HistorySmsSendService;
import com.example.onekids_project.service.servicecustom.SmsSendCustomService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * tin nhắn tùy chỉnh
 */
@RestController
@RequestMapping("/web/smscustom")
public class SmsCustomController {
    private static final Logger logger = LoggerFactory.getLogger(SmsCustomController.class);

    @Autowired
    HistorySmsSendService historySmsSendService;

    @Autowired
    SmsSendCustomService smsSendCustomService;

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchhistorySmsSend(@CurrentUser UserPrincipal principal, @Valid SearchSmsSendCustomRequest request) {
        RequestUtils.getFirstRequest(principal,request);
        ListSmsCustomResponse response = smsSendCustomService.searchSmsCustom(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    // xem chi tiết
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findMessageTeacherDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal,id);
        List<SmsCustomResponse> responseList = smsSendCustomService.findDetailHistory(principal, id);
        return NewDataResponse.setDataSearch(responseList);
    }

    // xem chi tiết
    @RequestMapping(method = RequestMethod.GET, value = "/fail/{id}")
    public ResponseEntity findMessageTeacherDetailFail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal,id);
        List<SmsCustomResponse> responseList = smsSendCustomService.findDetailHistoryFail(principal, id);
        return NewDataResponse.setDataSearch(responseList);
    }

    // xem chi tiết
    @RequestMapping(method = RequestMethod.GET, value = "/detailall/{id}")
    public ResponseEntity findMessageTeacherDetailAll(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal,id);
        List<SmsCustomResponse> responseList = smsSendCustomService.findDetailHistoryAll(principal, id);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * Gửi sms custom
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-sms")
    public ResponseEntity sendSmsByFile(@CurrentUser UserPrincipal principal, @Valid @RequestBody SmsCustomRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        smsSendCustomService.sendSmsCustom(principal, request);
        return NewDataResponse.setSendSms();
    }
    /**
     * Gửi app custom
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-firebase")
    public ResponseEntity sendFirebaseByFile(@CurrentUser UserPrincipal principal, @Valid @RequestBody SmsCustomRequest request) throws ExecutionException, InterruptedException, FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        smsSendCustomService.sendFirebaseCustom(principal, request);
        return NewDataResponse.setSendApp();
    }
}
