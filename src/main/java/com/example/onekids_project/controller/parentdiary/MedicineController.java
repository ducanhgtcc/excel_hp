package com.example.onekids_project.controller.parentdiary;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.entity.parent.MedicineAttachFile;
import com.example.onekids_project.repository.MedicineAttachFileRepository;
import com.example.onekids_project.request.common.ContentRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.parentdiary.MedicineRequest;
import com.example.onekids_project.request.parentdiary.SearchMedicineRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.parentdiary.ListMedicineResponse;
import com.example.onekids_project.response.parentdiary.MedicineNewResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.MedicineSerVice;
import com.example.onekids_project.util.RequestUtils;
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

@RequestMapping("/web/medicine")
public class MedicineController {
    private static final Logger logger = LoggerFactory.getLogger(MedicineController.class);
    @Autowired
    MedicineSerVice medicineSerVice;

    @Autowired
    private MedicineAttachFileRepository medicineAttachFileRepository;


    // search
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity findMedi(@CurrentUser UserPrincipal principal, SearchMedicineRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        ListMedicineResponse listMedicineResponse = medicineSerVice.searchMedicineAbc(principal, principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(listMedicineResponse);
    }

    @RequestMapping(value = "/medicinedowload/{idUrlFileFeedback}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download2(HttpServletRequest request, @PathVariable("idUrlFileFeedback") Long idUrlFileFeedback) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        MedicineAttachFile medicineAttachFile = medicineAttachFileRepository.findById(idUrlFileFeedback).get();
        try {
            File file = null;
            if (StringUtils.isNotBlank(medicineAttachFile.getUrlLocal())) {
                file = new File(medicineAttachFile.getUrlLocal());
            } else if (StringUtils.isNotBlank(medicineAttachFile.getUrlLocal())) {
                file = new File(medicineAttachFile.getUrlLocal());
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

    @RequestMapping(method = RequestMethod.PUT, value = "/save/reply")
    public ResponseEntity saveReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody ContentRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, request);
        boolean response = medicineSerVice.updateMedicine(principal.getIdSchoolLogin(), principal, request);
        return NewDataResponse.setDataCustom(response, MessageWebConstant.SUCCESS_SAVE_REPLY);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/confirm/{id}")
    public ResponseEntity confirmParentReply(@PathVariable Long id, @CurrentUser UserPrincipal principal) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, id);
        boolean response = medicineSerVice.confirmReply(principal, id);
        return NewDataResponse.setDataConfirm();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/teacher")
    public ResponseEntity revokeTeacher(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean response = medicineSerVice.revokeTeacher(principal, request);
        String message = request.getStatus() ? MessageWebConstant.SUCCESS_REVOKE : MessageWebConstant.SUCCESS_UNREVOKE;
        return NewDataResponse.setDataCustom(response, message);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/plus")
    public ResponseEntity revokePlus(@CurrentUser UserPrincipal principal, @Valid @RequestBody StatusRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean response = medicineSerVice.revokePlus(principal, request);
        String message = request.getStatus() ? MessageWebConstant.SUCCESS_REVOKE : MessageWebConstant.SUCCESS_UNREVOKE;
        return NewDataResponse.setDataCustom(response, message);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity findById(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        MedicineNewResponse response = medicineSerVice.findByIdMedicineNew(principal, id);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update-many-read")
    public ResponseEntity updateRead(@RequestBody List<MedicineRequest> medicineResponse, Long id) {
        boolean medicineRequest = medicineSerVice.updateRead(id, medicineResponse);
        return NewDataResponse.setDataUpdate(medicineRequest);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update-many-confirm")
    public ResponseEntity updateConfirm(@RequestBody List<MedicineRequest> medicineResponse, Long id, @CurrentUser UserPrincipal principal) throws FirebaseMessagingException {
        medicineSerVice.updateConfirmMany(id, medicineResponse, principal);
        return NewDataResponse.setDataConfirm();
    }


}
