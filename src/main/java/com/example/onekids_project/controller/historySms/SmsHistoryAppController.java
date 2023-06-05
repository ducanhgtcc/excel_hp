package com.example.onekids_project.controller.historySms;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.SmsSendDTO;
import com.example.onekids_project.request.notifihistory.SearchSmsSendHistoryRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.notifihistory.ListSmsSendHistoryResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SmsSendHistoryService;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/web/smshistoryapp")
public class SmsHistoryAppController {
    private static final Logger logger = LoggerFactory.getLogger(SmsHistoryAppController.class);
    @Autowired
    private SmsSendHistoryService smsSendHistoryService;

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        //thực hiện tìm kiếm theo id
        Optional<SmsSendDTO> smsSendDTOOptional = smsSendHistoryService.findByIdSmsSendHistory(idSchoolLogin, id);
        return NewDataResponse.setDataSearch(smsSendDTOOptional);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity findContent(@CurrentUser UserPrincipal principal, SearchSmsSendHistoryRequest searchSmsSendHistoryRequest) {
        RequestUtils.getFirstRequest(principal, searchSmsSendHistoryRequest);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        ListSmsSendHistoryResponse listSmsSendHistoryResponse = smsSendHistoryService.searchSmsSendHistory(idSchoolLogin, searchSmsSendHistoryRequest);
        return NewDataResponse.setDataSearch(listSmsSendHistoryResponse);
    }


}
