package com.example.onekids_project.controller.parentdiary;

import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.entity.parent.MessageParentAttachFile;
import com.example.onekids_project.repository.MessageParentAttachFileRepository;
import com.example.onekids_project.request.common.ContentRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.parentdiary.MessageParentRequest;
import com.example.onekids_project.request.parentdiary.SearchMessageParentRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.parentdiary.ListMessageParentResponse;
import com.example.onekids_project.response.parentdiary.MessageNewResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.MessageParentSerVice;
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
@RequestMapping("/web/message")
public class MessageParentController {
    private static final Logger logger = LoggerFactory.getLogger(MessageParentController.class);
    @Autowired
    MessageParentSerVice messageParentSerVice;

    @Autowired
    private MessageParentAttachFileRepository messageParentAttachFileRepository;

    @RequestMapping(value = "/messagedowload/{idUrlFileFeedback}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download2(HttpServletRequest request, @PathVariable("idUrlFileFeedback") Long idUrlFileFeedback) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        MessageParentAttachFile messageParentAttachFile = messageParentAttachFileRepository.findById(idUrlFileFeedback).get();
        try {
            File file = null;
            if (StringUtils.isNotBlank(messageParentAttachFile.getUrlLocal())) {
                file = new File(messageParentAttachFile.getUrlLocal());
            } else if (StringUtils.isNotBlank(messageParentAttachFile.getUrlLocal())) {
                file = new File(messageParentAttachFile.getUrlLocal());
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

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchMessage(@CurrentUser UserPrincipal principal, @Valid SearchMessageParentRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListMessageParentResponse response = messageParentSerVice.searchMessageParent(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update-many-read")
    public ResponseEntity updateRead(@RequestBody List<MessageParentRequest> messageParentResponse, Long id) {
        MessageParentRequest messageParentRequest = messageParentSerVice.updateRead(id, messageParentResponse);
        return NewDataResponse.setDataCustom(messageParentRequest, MessageWebConstant.READ_SUCCESS);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update-many-confirm")
    public ResponseEntity updateConfirm(@RequestBody List<MessageParentRequest> messageParentResponse, @CurrentUser UserPrincipal principal, Long id) throws FirebaseMessagingException {
        messageParentSerVice.updateConfirmMany(id, principal, messageParentResponse);
        return NewDataResponse.setDataConfirm();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/save/reply")
    public ResponseEntity saveReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody ContentRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, request);
        boolean response = messageParentSerVice.updateMessage(principal.getIdSchoolLogin(), principal, request);
        return NewDataResponse.setDataCustom(response, MessageWebConstant.SUCCESS_SAVE_REPLY);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/confirm/{id}")
    public ResponseEntity confirmParentReply(@PathVariable Long id, @CurrentUser UserPrincipal principal) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkPlusOrTeacher(principal);
        boolean response = messageParentSerVice.confirmReply(principal, id);
        return NewDataResponse.setDataConfirm();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/teacher")
    public ResponseEntity revokeTeacher(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean response = messageParentSerVice.revokeTeacher(principal, request);
        String message = request.getStatus() ? MessageWebConstant.SUCCESS_REVOKE : MessageWebConstant.SUCCESS_UNREVOKE;
        return NewDataResponse.setDataCustom(response, message);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/plus")
    public ResponseEntity revokePlus(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean response = messageParentSerVice.revokePlus(principal, request);
        String message = request.getStatus() ? MessageWebConstant.SUCCESS_REVOKE : MessageWebConstant.SUCCESS_UNREVOKE;
        return NewDataResponse.setDataCustom(response, message);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findByIdNew(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        MessageNewResponse response = messageParentSerVice.findByIdMessageNew(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

}
