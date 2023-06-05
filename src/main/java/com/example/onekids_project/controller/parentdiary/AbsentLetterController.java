package com.example.onekids_project.controller.parentdiary;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.dto.AbsentDateDTO;
import com.example.onekids_project.entity.parent.AbsentLetterAttachFile;
import com.example.onekids_project.repository.AbsentLetterAttachFileRepository;
import com.example.onekids_project.request.common.ContentRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.parentdiary.AbsentLetterRequest;
import com.example.onekids_project.request.parentdiary.SearchAbsentLetterRequest;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.parentdiary.AbsentNewResponse;
import com.example.onekids_project.response.parentdiary.ListAbsentLetterResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AbsentDateSerVice;
import com.example.onekids_project.service.servicecustom.AbsentLetterSerVice;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.List;

@RestController

@RequestMapping("/web/absent-letter")
public class AbsentLetterController {
    private static final Logger logger = LoggerFactory.getLogger(AbsentLetterController.class);
    @Autowired
    private AbsentLetterSerVice absentLetterSerVice;
    @Autowired
    private AbsentDateSerVice absentDateSerVice;

    @Autowired
    private AbsentLetterAttachFileRepository absentLetterAttachFileRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchAbsentLetter(@CurrentUser UserPrincipal principal, SearchAbsentLetterRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        ListAbsentLetterResponse listAbsentLetterResponse = absentLetterSerVice.searchAbsent(principal, request);
        return NewDataResponse.setDataSearch(listAbsentLetterResponse);

    }

    @RequestMapping(method = RequestMethod.PUT, value = "/save/reply")
    public ResponseEntity saveReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody ContentRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        boolean response = absentLetterSerVice.updateAbsent(principal.getIdSchoolLogin(), principal, request);
        return NewDataResponse.setDataCustom(response, MessageWebConstant.SUCCESS_SAVE_REPLY);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/confirm/{id}")
    public ResponseEntity confirmParentReply(@PathVariable Long id, @CurrentUser UserPrincipal principal) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkPlusOrTeacher(principal);
        absentLetterSerVice.confirmReply(principal, id);
        return NewDataResponse.setDataConfirm();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/teacher")
    public ResponseEntity revokeTeacher(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkPlusOrTeacher(principal);
        boolean response = absentLetterSerVice.revokeTeacher(principal, request);
        String message = request.getStatus() ? MessageWebConstant.SUCCESS_REVOKE : MessageWebConstant.SUCCESS_UNREVOKE;
        return NewDataResponse.setDataCustom(response, message);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/plus")
    public ResponseEntity revokePlus(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        CommonValidate.checkDataPlus(principal);
        boolean response = absentLetterSerVice.revokePlus(principal, request);
        String message = request.getStatus() ? MessageWebConstant.SUCCESS_REVOKE : MessageWebConstant.SUCCESS_UNREVOKE;
        return NewDataResponse.setDataCustom(response, message);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkPlusOrTeacher(principal);
        AbsentNewResponse response = absentLetterSerVice.findByIdAbsent(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update-many-read")
    public ResponseEntity updateRead(@CurrentUser UserPrincipal principal, @RequestBody List<AbsentLetterRequest> absentletterResponse, Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkPlusOrTeacher(principal);
        boolean absentLetterRequest = absentLetterSerVice.updateRead(id, absentletterResponse);
        return NewDataResponse.setDataUpdate(absentLetterRequest);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update-many-confirm")
    public ResponseEntity updateConfirm(@RequestBody List<AbsentLetterRequest> absentLetterRespone, Long id, @CurrentUser UserPrincipal principal) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkPlusOrTeacher(principal);
        absentLetterSerVice.updateConfirmMany(id, absentLetterRespone, principal);
        return NewDataResponse.setDataConfirm();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/date/{id}")
    public ResponseEntity getDateById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        List<AbsentDateDTO> responseList = absentDateSerVice.findByIdAbsentDate(principal.getIdSchoolLogin(), id);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(value = "/absentdowload/{idUrlFileAbsent}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download2(HttpServletRequest request, @PathVariable("idUrlFileAbsent") Long idUrlFileAbsent) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        AbsentLetterAttachFile absentLetterAttachFile = absentLetterAttachFileRepository.findById(idUrlFileAbsent).get();
        try {
            File file = null;
            if (StringUtils.isNotBlank(absentLetterAttachFile.getUrlLocal())) {
                file = new File(absentLetterAttachFile.getUrlLocal());
            } else if (StringUtils.isNotBlank(absentLetterAttachFile.getUrlLocal())) {
                file = new File(absentLetterAttachFile.getUrlLocal());
            }
            byte[] data = FileUtils.readFileToByteArray(file);
            // Set mimeType trả về
            responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // Thiết lập thông tin trả về
            responseHeader.set("Content-disposition", "attachment; filename=" + file.getName());
            responseHeader.setContentLength(data.length);
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            return new ResponseEntity<InputStreamResource>(inputStreamResource, responseHeader, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<InputStreamResource>(null, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
