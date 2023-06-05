package com.example.onekids_project.controller.birthday;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.EmployeeDTO;
import com.example.onekids_project.request.AppSend.CreateParentRealBirthdayRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchTeacherBirthDayRequest;
import com.example.onekids_project.request.birthdaymanagement.UpdateReiceiversRequest;
import com.example.onekids_project.response.appsend.AppSendResponse;
import com.example.onekids_project.response.birthdaymanagement.KidBirthdayResponse;
import com.example.onekids_project.response.birthdaymanagement.ListTeacherBirthDayResponse;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.example.onekids_project.service.servicecustom.TeacherBirthdayService;
import com.example.onekids_project.util.RequestUtils;
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

@RequestMapping("/web/teacher-birthday")
public class TeacherBirthDayController {
    private static final Logger logger = LoggerFactory.getLogger(TeacherBirthDayController.class);
    @Autowired
    private TeacherBirthdayService teacherBirthdayService;
    @Autowired
    private AppSendService appSendService;

    @RequestMapping(method = RequestMethod.GET, value = "/searchnewa")
    public ResponseEntity searchMessage(@CurrentUser UserPrincipal principal, @Valid SearchTeacherBirthDayRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListTeacherBirthDayResponse response = teacherBirthdayService.searchTeacherBirthdayNewa(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        Long idSchoolLogin = principal.getIdSchoolLogin();
        Optional<EmployeeDTO> employeeDTOOptional = teacherBirthdayService.findByIdEmployee(principal, idSchoolLogin, id);
        return NewDataResponse.setDataSearch(employeeDTOOptional);

    }

    /**
     *
     * @param id
     * @param principal
     * @param updateReiceiversEditRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity update(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateReiceiversRequest updateReiceiversEditRequest) {
        RequestUtils.getFirstRequest(principal, updateReiceiversEditRequest);
        //          kiểm tra id trên url và id trong đối tượng có khớp nhau không
        if (!updateReiceiversEditRequest.getId().equals(id)) {
            logger.error(AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR);
            return ErrorResponse.errorData("Không khớp id=" + id + " và id=" + updateReiceiversEditRequest.getId(), AppConstant.ID_URL_OBJECT_DIFFERENT_ERROR, HttpStatus.MULTIPLE_CHOICES);
        }
        KidBirthdayResponse kidBirthdayResponse = teacherBirthdayService.updateApprove(principal.getIdSchoolLogin(), principal, updateReiceiversEditRequest);
        return NewDataResponse.setDataUpdate(kidBirthdayResponse);

    }

    /**
     * Gửi lời chúc sinh nhật giáo viên
     *
     * @param principal
     * @param createParentRealBirthdayRequest
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/teacher")
    public ResponseEntity createTeacher(@CurrentUser UserPrincipal principal, @Valid @RequestBody CreateParentRealBirthdayRequest createParentRealBirthdayRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, createParentRealBirthdayRequest);
        AppSendResponse appSendResponse = appSendService.createAppsendTeacherBirthday(principal, createParentRealBirthdayRequest);
        return NewDataResponse.setDataCustom(appSendResponse, "Gửi lời chúc thành công");
    }

}
