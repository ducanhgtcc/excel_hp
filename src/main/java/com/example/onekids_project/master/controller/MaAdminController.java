package com.example.onekids_project.master.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.master.request.admin.MaAdminCreateRequest;
import com.example.onekids_project.master.request.admin.MaAdminSearchRequest;
import com.example.onekids_project.master.request.admin.MaAdminUpdateRequest;
import com.example.onekids_project.master.response.admin.AdminForSchoolResponse;
import com.example.onekids_project.master.response.admin.MaAdminResponse;
import com.example.onekids_project.master.response.admin.MaAdminSchoolResponse;
import com.example.onekids_project.master.service.MaAdminService;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.payload.MaUserActiveRequest;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/web/admin")
public class MaAdminController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MaAdminService maAdminService;

    @Autowired
    private MaUserService maUserService;

    /**
     * find all admin
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/employee")
    public ResponseEntity searchMaAdmin(MaAdminSearchRequest maAdminSearchRequest) {
        List<MaAdminResponse> maAdminResponseList = maAdminService.searchAllMaAdmin(maAdminSearchRequest);
        return NewDataResponse.setDataSearch(maAdminResponseList);
    }

    /**
     * create admin
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/employee")
    public ResponseEntity createMaAdminAndAccount(@Valid @RequestBody MaAdminCreateRequest maAdminCreateRequest) {
        MaAdminResponse maAdminResponse = maAdminService.createAdmin(maAdminCreateRequest);
        return NewDataResponse.setDataCreate(maAdminResponse);
    }

    /**
     * update admin
     *
     * @param maAdminUpdateRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/employee")
    public ResponseEntity updateMaAdmin(@Valid @RequestBody MaAdminUpdateRequest maAdminUpdateRequest) {
        MaAdminResponse maAdminResponse = maAdminService.udpateAdmin(maAdminUpdateRequest);
        return NewDataResponse.setDataUpdate(maAdminResponse);
    }

    /**
     * delete admin
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/employee/{id}")
    public ResponseEntity deleteMaAdmin(@PathVariable("id") Long id) {
        maAdminService.deleteAdmin(id);
        return NewDataResponse.setDataDelete(AppConstant.APP_TRUE);
    }

    /**
     * update active account admin
     *
     * @param maUserActiveRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/employee/account/active")
    public ResponseEntity updateMaAdmin(@Valid @RequestBody MaUserActiveRequest maUserActiveRequest) {
        boolean checkActive = maUserService.checkActiveUser(maUserActiveRequest);
        return NewDataResponse.setDataUpdate(checkActive);
    }

    /**
     * find school of admin
     *
     * @param maAdminSearchRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/employee-school")
    public ResponseEntity searchMaAdminSchool(MaAdminSearchRequest maAdminSearchRequest) {
        List<MaAdminSchoolResponse> maAdminSchoolResponseList = maAdminService.searchAdminSchool(maAdminSearchRequest);
        return NewDataResponse.setDataSearch(maAdminSchoolResponseList);
    }

    /**
     * find school of one admin
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/employee-school/{id}")
    public ResponseEntity findAdminOfSchool(@PathVariable("id") Long idAdmin) {
        List<AdminForSchoolResponse> adminForSchoolResponseList = maAdminService.findSchoolOfAdmin(idAdmin);
        return NewDataResponse.setDataSearch(adminForSchoolResponseList);
    }

    /**
     * update school of one admin
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/employee-school/{id}")
    public ResponseEntity updateAdminOfSchool(@PathVariable("id") Long idAdmin, @Valid @RequestBody List<IdObjectRequest> idObjectRequestList) {
        boolean checkUpdate = maAdminService.updateSchoolOfAdmin(idAdmin, idObjectRequestList);
        return NewDataResponse.setDataUpdate(checkUpdate);
    }
}
