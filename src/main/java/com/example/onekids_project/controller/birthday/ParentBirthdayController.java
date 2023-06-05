package com.example.onekids_project.controller.birthday;

import com.example.onekids_project.request.AppSend.CreateParentRealBirthdayRequest;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchParentBirthDayRequest;
import com.example.onekids_project.response.appsend.AppSendResponse;
import com.example.onekids_project.response.birthdaymanagement.ListParentBirthDayResponse;
import com.example.onekids_project.response.birthdaymanagement.ListParentResponse;
import com.example.onekids_project.response.birthdaymanagement.ParentsBirthdayResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.example.onekids_project.service.servicecustom.ParentBirthdayService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/web/parent-birthday")
public class ParentBirthdayController {
    private static final Logger logger = LoggerFactory.getLogger(ParentBirthdayController.class);
    @Autowired
    ParentBirthdayService parentBirthdayService;
    @Autowired
    AppSendService appSendService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findAll(@CurrentUser UserPrincipal principal, PageNumberWebRequest request) {
        ListParentResponse listParentResponse = parentBirthdayService.findAllParentBirthday(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(listParentResponse);
    }

    /**
     * Danh sách sinh nhật phụ huynh
     *
     * @param principal
     * @param searchParentBirthDayRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/searchnew")
    public ResponseEntity searchParentBirthday(@CurrentUser UserPrincipal principal, SearchParentBirthDayRequest searchParentBirthDayRequest) {
        CommonValidate.checkPlusOrTeacher(principal);
        RequestUtils.getFirstRequest(principal, searchParentBirthDayRequest);
        List<ParentsBirthdayResponse> parentsBirthdayResponseList = parentBirthdayService.searchParentBirthDayNew(principal, searchParentBirthDayRequest);
        return NewDataResponse.setDataSearch(parentsBirthdayResponseList);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/searchnewa")
    public ResponseEntity searchMessage(@CurrentUser UserPrincipal principal, @Valid SearchParentBirthDayRequest request) {
        CommonValidate.checkPlusOrTeacher(principal);
        RequestUtils.getFirstRequest(principal, request);
        ListParentBirthDayResponse response = parentBirthdayService.searchParentBirthDayNewa(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Gửi lời chúc sinh nhật phụ huynh
     *
     * @param principal
     * @param createParentRealBirthdayRequest
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/parentreal")
    public ResponseEntity createparent(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateParentRealBirthdayRequest createParentRealBirthdayRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, createParentRealBirthdayRequest);
        AppSendResponse appSendResponse = appSendService.createAppsendParentBirthday(principal, createParentRealBirthdayRequest);
        return NewDataResponse.setDataCustom(appSendResponse, "Gửi lời chúc thành công");

    }

}
