package com.example.onekids_project.controller;

import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.school.SchoolInfoResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SchoolInfoService;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * date 2021-03-05 15:25
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("web/school/info")
public class SchoolInfoController {
    @Autowired
    private SchoolInfoService schoolInfoService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity findOrderKidsDetail(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        SchoolInfoResponse response = schoolInfoService.findSchoolInfo(principal);
        return NewDataResponse.setDataSearch(response);
    }
}
