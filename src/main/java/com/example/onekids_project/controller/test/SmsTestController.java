package com.example.onekids_project.controller.test;

import com.example.onekids_project.integration.dto.SmsResultDTO;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.sms.SmsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/ut/sms")
public class SmsTestController {
    @Autowired
    private SmsService smsService;

    @RequestMapping(method = RequestMethod.POST, value = "/one")
    public ResponseEntity sendSms(@CurrentUser UserPrincipal principal, @RequestParam String phone) throws ExecutionException, InterruptedException {
        Long shoolId = principal.getSchool().getId();
        Future<SmsResultDTO> rs = smsService.sendSms( shoolId, phone, "Test guu cho mot so dien thoai");
        if (StringUtils.isNotBlank(rs.get().getErrMsg())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, rs.get().getErrMsg());
        } else {
            return DataResponse.getData(null, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/mutil")
    public ResponseEntity sendMutilSms(@CurrentUser UserPrincipal principal, @RequestParam String phones) throws ExecutionException, InterruptedException{
        Long shoolId = principal.getSchool().getId();

        String[] phoneArr = phones.split(";");
        List<String> listPhone = Arrays.asList(phoneArr);
        Future<List<SmsResultDTO>> rs = smsService.sendSms(shoolId, listPhone, "Test guu cho nhieu so dien thoai");
        if (StringUtils.isNotBlank(rs.get().get(0).getErrMsg())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, rs.get().get(0).getErrMsg());
        } else {
            return DataResponse.getData(null, HttpStatus.OK);
        }
    }
}
