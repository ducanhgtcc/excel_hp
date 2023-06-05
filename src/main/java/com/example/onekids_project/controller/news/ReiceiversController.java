package com.example.onekids_project.controller.news;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.ReiceiversDTO;
import com.example.onekids_project.request.AppSend.ReceiversRequest;
import com.example.onekids_project.request.notifihistory.UpdateSmsAppReiceiverAprovedRequest;
import com.example.onekids_project.request.notifihistory.UpdateSmsAppReiceiverRevokeRequest;
import com.example.onekids_project.response.appsend.ReceiversResponse;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.ReiceiverService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("/web/reiceivers")
public class ReiceiversController {
    private static final Logger logger = LoggerFactory.getLogger(ReiceiversController.class);
    @Autowired
    ReiceiverService reiceiverService;

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        Long idSchoolLogin = principal.getIdSchoolLogin();
        Optional<ReiceiversDTO> receiversDTOOptional = reiceiverService.findByidReiceivers(idSchoolLogin, id);
        return NewDataResponse.setDataSearch(receiversDTOOptional);
    }

    // approved
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity update(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateSmsAppReiceiverAprovedRequest updateSmsAppReiceiverAprovedRequest) {
        RequestUtils.getFirstRequest(principal, updateSmsAppReiceiverAprovedRequest);
        //kiểm tra id trên url và id trong đối tượng có khớp nhau không
        if (!updateSmsAppReiceiverAprovedRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + updateSmsAppReiceiverAprovedRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        ReceiversResponse receiversResponse = reiceiverService.updateApprove(principal.getIdSchoolLogin(), principal, updateSmsAppReiceiverAprovedRequest);
        return NewDataResponse.setDataUpdate(receiversResponse);
    }

    // revoke
    @RequestMapping(method = RequestMethod.PUT, value = "/a/{id}")
    public ResponseEntity updaterevoke(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateSmsAppReiceiverRevokeRequest updateSmsAppReiceiverRevokeRequest) {
        RequestUtils.getFirstRequest(principal, updateSmsAppReiceiverRevokeRequest);
        //kiểm tra id trên url và id trong đối tượng có khớp nhau không
        if (!updateSmsAppReiceiverRevokeRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + updateSmsAppReiceiverRevokeRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        ReceiversResponse receiversResponse = reiceiverService.updateRevoke1(principal.getIdSchoolLogin(), principal, updateSmsAppReiceiverRevokeRequest);
        return NewDataResponse.setDataUpdate(receiversResponse);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/b/{id}")
    public ResponseEntity updaterevokeun(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateSmsAppReiceiverRevokeRequest updateSmsAppReiceiverRevokeRequest) {
        RequestUtils.getFirstRequest(principal, updateSmsAppReiceiverRevokeRequest);
        CommonValidate.checkDataPlus(principal);
        //kiểm tra id trên url và id trong đối tượng có khớp nhau không
        if (!updateSmsAppReiceiverRevokeRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + updateSmsAppReiceiverRevokeRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        ReceiversResponse receiversResponse = reiceiverService.updateRevoke2(principal.getIdSchoolLogin(), principal, updateSmsAppReiceiverRevokeRequest);
        return NewDataResponse.setDataUpdate(receiversResponse);


    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAppsend(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkDelete = reiceiverService.deleteReiceivers(idSchoolLogin, id);
        return NewDataResponse.setDataDelete(checkDelete);

    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update-many-approved")
    public ResponseEntity updateRead(@CurrentUser UserPrincipal principal, @RequestBody List<ReceiversRequest> receiversRequests, Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        ReceiversRequest receiversRequest = reiceiverService.updateManyApprove(id, receiversRequests);
        return NewDataResponse.setDataUpdate(receiversRequest);
    }

}
