package com.example.onekids_project.master.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.controller.KidsController;
import com.example.onekids_project.master.request.kids.KidsSearchAdminRequest;
import com.example.onekids_project.master.response.kids.ListStudentAdminResponse;
import com.example.onekids_project.master.service.CommonMasterService;
import com.example.onekids_project.request.kids.MergeKidsRequest;
import com.example.onekids_project.request.kids.UpdateKidsAdminRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.KidsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/web/student-master")
public class KidsMasterController {

    private static final Logger logger = LoggerFactory.getLogger(KidsController.class);

    @Autowired
    private KidsService kidsService;

    @Autowired
    private CommonMasterService commonMasterService;

    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchKids(@CurrentUser UserPrincipal principal, @Valid KidsSearchAdminRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        ListStudentAdminResponse response = kidsService.searchKidsAdmin(request);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/extra")
    public ResponseEntity update(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateKidsAdminRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        boolean data = kidsService.updateKidExtraAdmin(request);
        return NewDataResponse.setDataCustom(data, MessageWebConstant.UPDATE_STUDENT);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/merge-kids")
    public ResponseEntity mergeKidsIntoParent(@CurrentUser UserPrincipal principal, @Valid @RequestBody MergeKidsRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        boolean check = kidsService.mergeKidIntoParent(request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.MERGE_KID);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity deleteKids(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), id);
        boolean check = kidsService.deleteKidsAdmin(id);
        return NewDataResponse.setDataDelete(check);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/restore/{id}")
    public ResponseEntity restoreKids(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), id);
        boolean check = kidsService.restoreKidsAdmin(id);
        return NewDataResponse.setDataRestore(check);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update/password")
    public ResponseEntity updatePasswordKids(@CurrentUser UserPrincipal principal, @RequestParam List<Long> idList, @RequestParam String newPassword) {
        logger.info("username: {}, fullName: {}, {}, {}", principal.getUsername(), principal.getFullName(), idList, newPassword);
        commonMasterService.updatePasswordManyKids(idList, newPassword);
        return NewDataResponse.setDataCustom(true, MessageConstant.UPDATE_PASSWORD);
    }
}
