package com.example.onekids_project.controller.historySms;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.request.notifihistory.SearchSmsAppRequest;
import com.example.onekids_project.request.notifihistory.SmsNotifiRequest;
import com.example.onekids_project.request.notifihistory.UpdateSmsAppRequest;
import com.example.onekids_project.request.notifihistory.UpdateSmsAppRevokeRequest;
import com.example.onekids_project.response.appsend.AppSendResponse;
import com.example.onekids_project.response.appsend.ListAppSendNewResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.notifihistory.ReiceiversResponeHistoru;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.example.onekids_project.service.servicecustom.SmsSendService;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * lịch sử thông báo app
 */
@RestController
@RequestMapping("/web/smsapp")
public class SmsAppController {
    private static final Logger logger = LoggerFactory.getLogger(SmsAppController.class);
    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private AppSendService appSendService;

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        //thực hiện tìm kiếm theo id
        List<ReiceiversResponeHistoru> appSendDTOOptional = appSendService.findByIdAppsend(principal, idSchoolLogin, id);
        return NewDataResponse.setDataSearch(appSendDTOOptional);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAppsend(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal, id);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkDelete = appSendService.deleteAppsend(principal, idSchoolLogin, id);
        return NewDataResponse.setDataDelete(checkDelete);

    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity update(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateSmsAppRequest smsAppRequestEditRequest) {
        RequestUtils.getFirstRequest(principal, smsAppRequestEditRequest);
        //kiểm tra id trên url và id trong đối tượng có khớp nhau không
        if (!smsAppRequestEditRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + smsAppRequestEditRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        AppSendResponse appSendResponse = smsSendService.updateSmsApp(principal.getIdSchoolLogin(), principal, smsAppRequestEditRequest);
        return NewDataResponse.setDataUpdate(appSendResponse);


    }

    @RequestMapping(method = RequestMethod.PUT, value = "a/{id}")
    public ResponseEntity update1(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateSmsAppRevokeRequest updateSmsAppRevokeRequest) {
        RequestUtils.getFirstRequest(principal, updateSmsAppRevokeRequest);
        //kiểm tra id trên url và id trong đối tượng có khớp nhau không
        if (!updateSmsAppRevokeRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + updateSmsAppRevokeRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        AppSendResponse appSendResponse = smsSendService.updateSmsAppforrevoke(principal.getIdSchoolLogin(), principal, updateSmsAppRevokeRequest);
        return NewDataResponse.setDataUpdate(appSendResponse);


    }

    @RequestMapping(method = RequestMethod.PUT, value = "b/{id}")
    public ResponseEntity update2(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateSmsAppRevokeRequest updateSmsAppRevokeRequest) {
        RequestUtils.getFirstRequest(principal, updateSmsAppRevokeRequest);
        //kiểm tra id trên url và id trong đối tượng có khớp nhau không
        if (!updateSmsAppRevokeRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + updateSmsAppRevokeRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        AppSendResponse appSendResponse = smsSendService.updateSmsAppforrevokeun(principal.getIdSchoolLogin(), principal, updateSmsAppRevokeRequest);
        return NewDataResponse.setDataUpdate(appSendResponse);


    }

    @RequestMapping(method = RequestMethod.GET, value = "/searchforteachernew")
    public ResponseEntity findContent(@CurrentUser UserPrincipal principal, SearchSmsAppRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListAppSendNewResponse response = smsSendService.searchSmsAppTeacherNew(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/searchforsysnew")
    public ResponseEntity searchMessage(@CurrentUser UserPrincipal principal, @Valid SearchSmsAppRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListAppSendNewResponse response = smsSendService.searchSmsAppSysNew(principal, request);
        return NewDataResponse.setDataSearch(response);
    }


    @DeleteMapping("/a/{id}")
    public ResponseEntity deleteAppsend(@CurrentUser UserPrincipal principal, @PathVariable(name = "id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        boolean checkDelete = smsSendService.deleteApp(id);
        return NewDataResponse.setDataDelete(checkDelete);

    }


    @RequestMapping(method = RequestMethod.PUT, value = "/update-many-read")
    public ResponseEntity updateRead(@CurrentUser UserPrincipal principal, @RequestBody List<SmsNotifiRequest> smsNotifiRespone, Long id) {
        RequestUtils.getFirstRequest(principal, id);
        SmsNotifiRequest smsNotifiRequest = smsSendService.updateApproved(id, smsNotifiRespone);
        return NewDataResponse.setDataUpdate(smsNotifiRequest);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update-many-approved")
    public ResponseEntity updateConfirm(@RequestBody List<SmsNotifiRequest> smsNotifiRequests, @CurrentUser UserPrincipal principal, Long id) {
        RequestUtils.getFirstRequest(principal, id);
        SmsNotifiRequest smsNotifiRequest = smsSendService.updateManyApproved(id, principal, smsNotifiRequests);
        return NewDataResponse.setDataUpdate(smsNotifiRequest);
    }


}
