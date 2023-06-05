package com.example.onekids_project.controller.historySms;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.request.notifihistory.SearchSmsSendRequest;
import com.example.onekids_project.request.smsNotify.SmsNotifyRequest;
import com.example.onekids_project.request.smsNotify.SmsSearchKidsRequest;
import com.example.onekids_project.request.smsNotify.VerifyCodeRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.kids.KidsSmsResponse;
import com.example.onekids_project.response.notifihistory.ListSmsSendResponse;
import com.example.onekids_project.response.notifihistory.ReiceiverSmsSchedule;
import com.example.onekids_project.response.sms.SmsConvertResponse;
import com.example.onekids_project.response.sms.SmsResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.EmployeeService;
import com.example.onekids_project.service.servicecustom.KidsService;
import com.example.onekids_project.service.servicecustom.SmsSendService;
import com.example.onekids_project.service.servicecustom.common.FindSmsService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * tin nhắn đặt lịch
 */
@RestController
@RequestMapping("/web/schedulesms")
public class SmsSendController {
    private static final Logger logger = LoggerFactory.getLogger(SmsSendController.class);
    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private FindSmsService findSmsService;

    @Autowired
    private KidsService kidsService;

    @Autowired
    private EmployeeService employeeService;


    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity findContent(@CurrentUser UserPrincipal principal, SearchSmsSendRequest searchSmsSendRequest) {
        RequestUtils.getFirstRequest(principal, searchSmsSendRequest);
        CommonValidate.checkDataPlus(principal);
        ListSmsSendResponse listSmsSendResponse = smsSendService.searchSmsSend(principal, searchSmsSendRequest);
        return NewDataResponse.setDataSearch(listSmsSendResponse);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findByIds(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataPlus(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        List<ReiceiverSmsSchedule> smsSendCaladarResponseList = smsSendService.findByIdSmsss(idSchoolLogin, id);
        return NewDataResponse.setDataSearch(smsSendCaladarResponseList);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAppsend(@PathVariable(name = "id") Long id) {
        boolean checkDelete = smsSendService.deleteSmsSend(id);
        return NewDataResponse.setDataDelete(checkDelete);
    }

    /**
     * create student Sms
     *
     * @param principal
     * @param
     * @return
     */
//    @Deprecated
//    @RequestMapping(method = RequestMethod.POST, value = "/student-sms")
//    public ResponseEntity createNotifyStudentSms(@CurrentUser UserPrincipal principal, @Valid SmsNotifyRequest smsNotifyRequest) throws ExecutionException, InterruptedException {
//        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), smsNotifyRequest);
//        boolean checkCreate = kidsService.createStudentNotifySms(principal, smsNotifyRequest);
//        return NewDataResponse.setDataCreate(checkCreate);
//    }

    /**
     * send account student Sms
     *
     * @param principal
     * @param
     * @return
     */
//    @Deprecated
//    @RequestMapping(method = RequestMethod.POST, value = "/student-sms/account")
//    public ResponseEntity sendAccountStudentSms(@CurrentUser UserPrincipal principal, @NotEmpty @RequestBody List<Long> idStudents) throws IOException, ExecutionException, InterruptedException {
//        RequestUtils.getFirstRequest(principal, idStudents);
//        CommonValidate.checkDataPlus(principal);
//        boolean checkCreate = kidsService.sendAccountStudentSms(principal, idStudents);
//        return NewDataResponse.setDataCustom(checkCreate, MessageConstant.SEND_ACCOUNT_STUDENT);
//    }


    /**
     * create employee Sms
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/employee-sms")
    public ResponseEntity createNotifyEmployeeSms(@CurrentUser UserPrincipal principal, @Valid SmsNotifyRequest smsNotifyRequest) throws IOException, ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, smsNotifyRequest);
        boolean checkCreate = employeeService.createEmployeeNotifySms(principal, smsNotifyRequest);
        return NewDataResponse.setDataCreate(checkCreate);
    }

    /**
     * find title sms
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/student-sms-title")
    public ResponseEntity findSmsTitle(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        SmsResponse titleSmsResponse = findSmsService.findSms(principal);
        return NewDataResponse.setDataSearch(titleSmsResponse);
    }

    /**
     * convertSms
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/convert-sms")
    public ResponseEntity convertSms(@CurrentUser UserPrincipal principal, @RequestBody String contentSms) {
        RequestUtils.getFirstRequest(principal, contentSms);
        SmsConvertResponse convertSmsResponse = findSmsService.convertSms(principal, contentSms);
        return NewDataResponse.setDataSearch(convertSmsResponse);
    }

    /**
     * create verify code Account
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/create-code")
    public ResponseEntity createVerifyAccountKid(@CurrentUser UserPrincipal principal, @Valid VerifyCodeRequest verifyCodeRequest) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, verifyCodeRequest);
        boolean checkSend = smsSendService.createVerifyAccountKid(principal, verifyCodeRequest);
        return NewDataResponse.setDataCustom(checkSend, MessageConstant.SEND_CODE_VERIFY_ACCOUNT_KID);
    }

    /**
     * tìm tất cả các học sinh theo idClass
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/find-kids-byClass")
    public ResponseEntity findAllKidsByIdClass(@CurrentUser UserPrincipal principal, SmsSearchKidsRequest smsSearchKidsRequest) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, smsSearchKidsRequest);
        List<KidsSmsResponse> kidsList = kidsService.seachKidsByClass(principal, smsSearchKidsRequest.getIdClassList());
        return NewDataResponse.setDataSearch(kidsList);
    }

    /**
     * tìm tất cả các học sinh theo idGrade
     *
     * @param principal
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/find-kids-byGrade")
    public ResponseEntity findAllKidsByIdGrade(@CurrentUser UserPrincipal principal, SmsSearchKidsRequest smsSearchKidsRequest) throws ExecutionException, InterruptedException {
        RequestUtils.getFirstRequest(principal, smsSearchKidsRequest);
        List<KidsSmsResponse> kidsList = kidsService.seachKidsByGrade(principal, smsSearchKidsRequest.getIdGradeList());
        return NewDataResponse.setDataSearch(kidsList);
    }


}
