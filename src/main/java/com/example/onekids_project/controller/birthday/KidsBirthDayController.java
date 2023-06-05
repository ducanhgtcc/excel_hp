package com.example.onekids_project.controller.birthday;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.KidsBirthdayDTO;
import com.example.onekids_project.request.AppSend.CreateSendParentBirthdayRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchKidsBirthDayRequest;
import com.example.onekids_project.request.birthdaymanagement.UpdateReiceiversRequest;
import com.example.onekids_project.response.birthdaymanagement.KidBirthdayResponse;
import com.example.onekids_project.response.birthdaymanagement.ListKidsBirthDayResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.example.onekids_project.service.servicecustom.KidsService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController

@RequestMapping("/web/kids-birthday")
public class KidsBirthDayController {
    private static final Logger logger = LoggerFactory.getLogger(KidsBirthDayController.class);
    @Autowired
    private KidsService kidsService;
    @Autowired
    private AppSendService appSendService;

    /**
     * Danh sách sinh nhật học sinh
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/searchnew")
    public ResponseEntity searchMessage(@CurrentUser UserPrincipal principal, @Valid SearchKidsBirthDayRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListKidsBirthDayResponse response = kidsService.searchKidsBirthDayNew(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkPlusOrTeacher(principal);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        Optional<KidsBirthdayDTO> kidsBirthdayDTOOptional = kidsService.findByIdKidb(principal, idSchoolLogin, id);
        return NewDataResponse.setDataSearch(kidsBirthdayDTOOptional);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity update(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateReiceiversRequest updateReiceiversEditRequest) {
        RequestUtils.getFirstRequest(principal, updateReiceiversEditRequest);
        //kiểm tra id trên url và id trong đối tượng có khớp nhau không
        if (!updateReiceiversEditRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + updateReiceiversEditRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        KidBirthdayResponse kidBirthdayResponse = kidsService.updateApprove(principal.getIdSchoolLogin(), principal, updateReiceiversEditRequest);
        return NewDataResponse.setDataUpdate(kidBirthdayResponse);
    }
    /**
     * Gửi lời chúc sinh nhật học sinh
     *
     * @param principal
     * @param createSendParentBirthdayRequest
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/kids")
    public ResponseEntity create(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateSendParentBirthdayRequest createSendParentBirthdayRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, createSendParentBirthdayRequest);
        boolean appSendResponse = appSendService.createAppsendParent(principal, createSendParentBirthdayRequest);
        return NewDataResponse.setDataCustom(appSendResponse, "Gửi lời chúc thành công");

    }
}
