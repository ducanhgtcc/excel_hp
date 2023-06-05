package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.plus.request.sms.SendAccountRequest;
import com.example.onekids_project.mobile.plus.request.sms.SendSmsFailRequest;
import com.example.onekids_project.mobile.plus.request.sms.SendSmsRequest;
import com.example.onekids_project.mobile.plus.service.servicecustom.SmsPlusService;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/mob/plus/sms")
public class SmsPlusController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SmsPlusService smsPlusService;

    /**
     * gửi tài khoản học sinh theo khối
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-account-student-grade")
    public ResponseEntity sendAccountStudentGrade(@CurrentUser UserPrincipal principal, @Valid @RequestBody SendAccountRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean response = smsPlusService.sendAccountStudentGrade(principal, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.SEND_ACCOUNT_STUDENT);

    }

    /**
     * gửi sms học sinh theo khối
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-student-grade")
    public ResponseEntity sendAccountStudentGrade(@CurrentUser UserPrincipal principal, @Valid @RequestBody SendSmsRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean response = smsPlusService.sendSmsStudentGrade(principal, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.SEND_SMS_STUDENT);

    }

    /**
     * gửi tài khoản học sinh theo lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-account-student-class")
    public ResponseEntity sendAccountStudentClass(@CurrentUser UserPrincipal principal, @Valid @RequestBody SendAccountRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean response = smsPlusService.sendAccountStudentClass(principal, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.SEND_ACCOUNT_STUDENT);

    }

    /**
     * gửi sms học sinh theo lớp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-student-class")
    public ResponseEntity sendSmsStudentClass(@CurrentUser UserPrincipal principal, @Valid @RequestBody SendSmsRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean response = smsPlusService.sendSmsStudentClass(principal, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.SEND_SMS_STUDENT);

    }

    /**
     * gửi tài khoản học sinh theo nhóm
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-account-student-group")
    public ResponseEntity sendAccountStudentGroup(@CurrentUser UserPrincipal principal, @Valid @RequestBody SendAccountRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean response = smsPlusService.sendAccountStudentGroup(principal, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.SEND_ACCOUNT_STUDENT);

    }

    /**
     * gửi sms học sinh theo nhóm
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-student-group")
    public ResponseEntity sendSmsStudentGroup(@CurrentUser UserPrincipal principal, @Valid @RequestBody SendSmsRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean response = smsPlusService.sendSmsStudentGroup(principal, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.SEND_SMS_STUDENT);

    }

    /**
     * gửi tài khoản nhiều học sinh
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-account-student-multi")
    public ResponseEntity sendAccountStudent(@CurrentUser UserPrincipal principal, @Valid @RequestBody SendAccountRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean response = smsPlusService.sendAccountStudentMulti(principal, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.SEND_ACCOUNT_STUDENT);

    }

    /**
     * gửi sms học sinh
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-student-multi")
    public ResponseEntity sendSmsStudentMulti(@CurrentUser UserPrincipal principal, @Valid @RequestBody SendSmsRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean response = smsPlusService.sendSmsStudentMulti(principal, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.SEND_SMS_STUDENT);

    }

    /**
     * gửi tài khoản một học sinh
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-account-student")
    public ResponseEntity sendAccountStudent(@CurrentUser UserPrincipal principal, @Valid @RequestBody IdRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean response = smsPlusService.sendAccountStudent(principal, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.SEND_ACCOUNT_STUDENT);

    }

    /**
     * tgửi tài khoản cho nhân viên theo phòng ban
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-account-employee-deparment")
    public ResponseEntity sendAccountEmployeeDeparment(@CurrentUser UserPrincipal principal, @Valid @RequestBody SendAccountRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean response = smsPlusService.sendAccountEmployeeDeparment(principal, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.SEND_ACCOUNT_EMPLOYEE);

    }

    /**
     * tgửi tài khoản cho nhân viên
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-account-employee-multi")
    public ResponseEntity sendAccountEmployee(@CurrentUser UserPrincipal principal, @Valid @RequestBody SendAccountRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean response = smsPlusService.sendAccountEmployeeMulti(principal, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.SEND_ACCOUNT_EMPLOYEE);

    }

    /**
     * tgửi sms cho nhân viên theo phòng ban
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-sms-employee-deparment")
    public ResponseEntity sendSmsEmployee(@CurrentUser UserPrincipal principal, @Valid @RequestBody SendSmsRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean response = smsPlusService.sendSmsEmployeeDeparment(principal, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.SEND_ACCOUNT_EMPLOYEE);

    }

    /**
     * gửi sms cho các nhân viên
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-sms-employee-multi")
    public ResponseEntity sendSmsEmployeeMulti(@CurrentUser UserPrincipal principal, @Valid @RequestBody SendSmsRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean response = smsPlusService.sendSmsEmployeeMulti(principal, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.SEND_ACCOUNT_EMPLOYEE);

    }

    /**
     * gửi lại sms lỗi
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/send-sms-fail")
    public ResponseEntity sendSmsEmployeeMulti(@CurrentUser UserPrincipal principal, @Valid @RequestBody SendSmsFailRequest request) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequestPlus(principal, request);
        boolean response = smsPlusService.sendSmsFail(principal, request);
        return NewDataResponse.setDataCustom(response, MessageConstant.SEND_SMS_FAIL);

    }

}
