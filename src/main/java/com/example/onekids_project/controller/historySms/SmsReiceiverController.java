package com.example.onekids_project.controller.historySms;

import com.example.onekids_project.dto.SmsReiceiveDTO;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SmsReiceriversService;
import com.example.onekids_project.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/web/smsreiceive")
public class SmsReiceiverController {
    private static final Logger logger = LoggerFactory.getLogger(SmsReiceiverController.class);
    @Autowired
    private SmsReiceriversService smsReiceriversService;

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        SmsReiceiveDTO smsReceiveDTOOptional = smsReiceriversService.findByIdSmsReiceiver(principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataSearch(smsReceiveDTOOptional);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteAppsend(@CurrentUser UserPrincipal principal, @PathVariable(name = "id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        boolean checkDelete = smsReiceriversService.deleteSmsReiceivers(id);
        return NewDataResponse.setDataDelete(checkDelete);

    }

    @DeleteMapping("/delete-multi")
    public ResponseEntity deleteMulti(@RequestBody Long[] ids, @CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal, ids);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        boolean checkDelete = smsReiceriversService.deleteMultiSms(idSchoolLogin, ids);
        return NewDataResponse.setDataDelete(checkDelete);

    }


}
